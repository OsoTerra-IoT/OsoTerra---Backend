package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.util.List;

/**
 * Resolves a plot's alert history. Implemented by an anti-corruption layer client into
 * Salinity Alerting.
 */
public interface AlertHistoryLookup {

    List<AlertHistoryEntry> findAlertHistoryForPlot(Long plotId);
}
