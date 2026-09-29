package com.osoterra.ososense.salinityalerting.domain.gateways;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Resolves the salinity tolerance threshold that applies to a plot, via its assigned
 * crop. Implemented by an anti-corruption layer client into Farm Management, which owns
 * both the plot-crop assignment and the crop catalog.
 */
public interface CropThresholdLookup {

    Optional<BigDecimal> findThresholdForPlot(Long plotId);
}
