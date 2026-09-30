package com.osoterra.ososense.salinityalerting.domain.gateways;

import java.util.Optional;

/**
 * Resolves the IAM user account id that owns a plot (through its farm), so a generated
 * alert knows who to notify. Implemented by an anti-corruption layer client into Farm
 * Management.
 */
public interface PlotOwnerLookup {

    Optional<Long> findOwnerIdForPlot(Long plotId);
}
