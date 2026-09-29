package com.osoterra.ososense.analyticsreporting.application.internal.queryservices;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.model.ReportSection;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.repositories.PlotReportRepository;
import com.osoterra.ososense.analyticsreporting.domain.repositories.ReportSectionRepository;
import com.osoterra.ososense.analyticsreporting.domain.repositories.SalinityTrendRepository;
import com.osoterra.ososense.analyticsreporting.domain.services.AnalyticsReportingQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class AnalyticsReportingQueryServiceImpl implements AnalyticsReportingQueryService {

    private final SalinityTrendRepository salinityTrendRepository;
    private final PlotReportRepository plotReportRepository;
    private final ReportSectionRepository reportSectionRepository;

    AnalyticsReportingQueryServiceImpl(
            SalinityTrendRepository salinityTrendRepository, PlotReportRepository plotReportRepository,
            ReportSectionRepository reportSectionRepository) {
        this.salinityTrendRepository = salinityTrendRepository;
        this.plotReportRepository = plotReportRepository;
        this.reportSectionRepository = reportSectionRepository;
    }

    @Override
    public List<SalinityTrend> findTrendsByPlotId(Long plotId) {
        return salinityTrendRepository.findByPlotId(plotId);
    }

    @Override
    public Optional<PlotReport> findReportById(PlotReportId id) {
        return plotReportRepository.findById(id);
    }

    @Override
    public List<PlotReport> findReportsByPlotId(Long plotId) {
        return plotReportRepository.findByPlotId(plotId);
    }

    @Override
    public List<ReportSection> findSectionsByReportId(PlotReportId reportId) {
        return reportSectionRepository.findByPlotReportIdOrderByDisplayOrderAsc(reportId);
    }
}
