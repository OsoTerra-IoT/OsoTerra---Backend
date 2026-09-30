package com.osoterra.ososense.analyticsreporting.application.acl;

import com.osoterra.ososense.analyticsreporting.domain.gateways.AlertHistoryEntry;
import com.osoterra.ososense.analyticsreporting.domain.gateways.AlertHistoryLookup;
import com.osoterra.ososense.salinityalerting.domain.services.SalinityAlertingQueryService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Anti-corruption layer resolving a plot's alert history from Salinity Alerting,
 * translated into this context's own {@link AlertHistoryEntry} shape.
 */
@Component
class SalinityAlertingHistoryClient implements AlertHistoryLookup {

    private final SalinityAlertingQueryService salinityAlertingQueryService;

    SalinityAlertingHistoryClient(SalinityAlertingQueryService salinityAlertingQueryService) {
        this.salinityAlertingQueryService = salinityAlertingQueryService;
    }

    @Override
    public List<AlertHistoryEntry> findAlertHistoryForPlot(Long plotId) {
        return salinityAlertingQueryService.findAlertsByPlotId(plotId).stream()
                .map(alert -> new AlertHistoryEntry(
                        alert.getSeverity().name(), alert.getStatus().name(), alert.getGeneratedAt()))
                .toList();
    }
}
