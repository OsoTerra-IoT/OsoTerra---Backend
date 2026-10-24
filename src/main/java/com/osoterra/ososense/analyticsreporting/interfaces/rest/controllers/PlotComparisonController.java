package com.osoterra.ososense.analyticsreporting.interfaces.rest.controllers;

import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotStructureLookup;
import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotSummary;
import com.osoterra.ososense.analyticsreporting.domain.gateways.SoilReadingSeriesLookup;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.PlotSeriesResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.SeriesPointResource;
import com.osoterra.ososense.shared.domain.services.PlotAccessPolicy;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Compares the compensated-conductivity series of 2 to 4 plots over the same period,
 * side by side.
 */
@RestController
@RequestMapping("/api/v1/plot-comparisons")
class PlotComparisonController {

    private final SoilReadingSeriesLookup soilReadingSeriesLookup;
    private final PlotStructureLookup plotStructureLookup;
    private final PlotAccessPolicy plotAccessPolicy;

    PlotComparisonController(SoilReadingSeriesLookup soilReadingSeriesLookup, PlotStructureLookup plotStructureLookup, PlotAccessPolicy plotAccessPolicy) {
        this.soilReadingSeriesLookup = soilReadingSeriesLookup;
        this.plotStructureLookup = plotStructureLookup;
        this.plotAccessPolicy = plotAccessPolicy;
    }

    @GetMapping
    List<PlotSeriesResource> compare(
            @RequestParam List<Long> plotIds, @RequestParam LocalDate periodStart, @RequestParam LocalDate periodEnd,
            @CurrentUserId Long userId) {
        if (plotIds.size() < 2 || plotIds.size() > 4) {
            throw new IllegalArgumentException("A comparison requires between 2 and 4 plots");
        }
        plotIds.forEach(plotId -> plotAccessPolicy.checkPlotRead(userId, plotId));
        return plotIds.stream().map(plotId -> toPlotSeries(plotId, periodStart, periodEnd)).toList();
    }

    private PlotSeriesResource toPlotSeries(Long plotId, LocalDate periodStart, LocalDate periodEnd) {
        String plotName = plotStructureLookup.findPlotSummary(plotId).map(PlotSummary::plotName).orElse(null);
        List<SeriesPointResource> points = soilReadingSeriesLookup.findSeriesForPlot(plotId, periodStart, periodEnd)
                .stream()
                .map(point -> new SeriesPointResource(point.capturedAt(), point.compensatedConductivityDsM()))
                .toList();
        return new PlotSeriesResource(plotId, plotName, points);
    }
}
