package com.osoterra.ososense.analyticsreporting.interfaces.rest.controllers;

import com.osoterra.ososense.analyticsreporting.domain.gateways.AlertHistoryLookup;
import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotStructureLookup;
import com.osoterra.ososense.analyticsreporting.domain.gateways.PlotSummary;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.AlertSummaryResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.PlotDashboardResource;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A farmer's single-plot summary, or an advisor's read-only view into a client's plot —
 * assembled entirely from anti-corruption layer clients into the other bounded contexts,
 * with no aggregate of its own.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
class DashboardController {

    private final PlotStructureLookup plotStructureLookup;
    private final AlertHistoryLookup alertHistoryLookup;

    DashboardController(PlotStructureLookup plotStructureLookup, AlertHistoryLookup alertHistoryLookup) {
        this.plotStructureLookup = plotStructureLookup;
        this.alertHistoryLookup = alertHistoryLookup;
    }

    @GetMapping("/plots/{plotId}")
    PlotDashboardResource plotDashboard(@PathVariable Long plotId) {
        PlotSummary summary = plotStructureLookup
                .findPlotSummary(plotId)
                .orElseThrow(() -> new EntityNotFoundException("Plot not found for id " + plotId));
        List<AlertSummaryResource> recentAlerts = alertHistoryLookup.findAlertHistoryForPlot(plotId).stream()
                .map(entry -> new AlertSummaryResource(entry.severity(), entry.status(), entry.generatedAt()))
                .toList();
        return new PlotDashboardResource(
                summary.plotId(), summary.plotName(), summary.areaHectares(), summary.cropName(), recentAlerts);
    }
}
