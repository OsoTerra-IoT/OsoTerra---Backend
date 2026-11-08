package com.osoterra.ososense.subscriptionbilling.domain.gateways;

/**
 * Counts the active plots an account holder currently conducts, which is what their
 * subscription's plot quota is measured against. Implemented by an anti-corruption layer
 * client into Farm Management.
 */
public interface ActivePlotCounter {

    int countActivePlotsOfOwner(Long ownerId);
}
