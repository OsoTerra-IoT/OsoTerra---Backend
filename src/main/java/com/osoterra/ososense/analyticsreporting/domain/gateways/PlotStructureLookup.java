package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.util.Optional;

/**
 * Resolves a plot's structural summary (farm, name, area, assigned crop). Implemented
 * by an anti-corruption layer client into Farm Management.
 */
public interface PlotStructureLookup {

    Optional<PlotSummary> findPlotSummary(Long plotId);
}
