package com.osoterra.ososense.farmmanagement.domain.gateways;

/**
 * Tells whether an advisor has an accepted, active advisory link with a farmer, which is
 * what grants the advisor read access to that farmer's farms. Implemented by an
 * anti-corruption layer client into IAM.
 */
public interface AdvisoryAccessLookup {
    boolean isLinked(Long advisorId, Long farmerId);
}
