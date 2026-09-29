package com.osoterra.ososense.analyticsreporting.application.internal.commandservices;

import com.osoterra.ososense.analyticsreporting.domain.gateways.AlertHistoryEntry;
import com.osoterra.ososense.analyticsreporting.domain.gateways.AlertHistoryLookup;
import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotStructureLookup;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.repositories.PlotReportRepository;
import com.osoterra.ososense.analyticsreporting.domain.repositories.ReportSectionRepository;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommand;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommandService;
import com.osoterra.ososense.analyticsreporting.domain.services.GeneratePlotReportCommand;
import com.osoterra.ososense.analyticsreporting.domain.services.GeneratePlotReportCommandService;
import com.osoterra.ososense.shared.BusinessRuleViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembles a plot report from a fixed set of sections, each pulled through this
 * context's anti-corruption layer clients into the other bounded contexts it reports on.
 */
@Service
class GeneratePlotReportCommandServiceImpl implements GeneratePlotReportCommandService {

    private final PlotReportRepository plotReportRepository;
    private final ReportSectionRepository reportSectionRepository;
    private final PlotStructureLookup plotStructureLookup;
    private final AlertHistoryLookup alertHistoryLookup;
    private final ComputeSalinityTrendCommandService computeSalinityTrendCommandService;

    GeneratePlotReportCommandServiceImpl(
            PlotReportRepository plotReportRepository,
            ReportSectionRepository reportSectionRepository,
            PlotStructureLookup plotStructureLookup,
            AlertHistoryLookup alertHistoryLookup,
            ComputeSalinityTrendCommandService computeSalinityTrendCommandService) {
        this.plotReportRepository = plotReportRepository;
        this.reportSectionRepository = reportSectionRepository;
        this.plotStructureLookup = plotStructureLookup;
        this.alertHistoryLookup = alertHistoryLookup;
        this.computeSalinityTrendCommandService = computeSalinityTrendCommandService;
    }

    @Override
    public PlotReport handle(GeneratePlotReportCommand command) {
        PlotReport report = plotReportRepository.save(PlotReport.generate(
                command.plotId(), command.generatedBy(), command.periodStart(), command.periodEnd()));

        int displayOrder = 1;
        reportSectionRepository.save(ReportSection.create(
                report.getId(), "Summary", "SUMMARY", buildSummary(command.plotId()), displayOrder++));
        reportSectionRepository.save(ReportSection.create(
                report.getId(), "Salinity Trend", "TREND", buildTrend(command), displayOrder++));
        reportSectionRepository.save(ReportSection.create(
                report.getId(), "Alert History", "ALERT_HISTORY", buildAlertHistory(command.plotId()), displayOrder));

        return report;
    }

    private String buildSummary(Long plotId) {
        return plotStructureLookup
                .findPlotSummary(plotId)
                .map(summary -> "Plot " + summary.plotName() + " (" + summary.areaHectares() + " ha), crop: "
                        + (summary.cropName() == null ? "none assigned" : summary.cropName()))
                .orElse("Plot structure not found.");
    }

    private String buildTrend(GeneratePlotReportCommand command) {
        try {
            SalinityTrend trend = computeSalinityTrendCommandService.handle(new ComputeSalinityTrendCommand(
                    command.plotId(), command.periodStart(), command.periodEnd()));
            return "Slope: " + trend.getSlopeDsMPerDay() + " dS/m per day (" + trend.getDirection() + "), based on "
                    + trend.getReadingCount() + " readings.";
        } catch (BusinessRuleViolationException insufficientReadings) {
            return insufficientReadings.getMessage();
        }
    }

    private String buildAlertHistory(Long plotId) {
        List<AlertHistoryEntry> entries = alertHistoryLookup.findAlertHistoryForPlot(plotId);
        if (entries.isEmpty()) {
            return "No alerts recorded for this period.";
        }
        return entries.stream()
                .map(entry -> entry.severity() + " - " + entry.status() + " (" + entry.generatedAt() + ")")
                .collect(Collectors.joining("\n"));
    }
}
