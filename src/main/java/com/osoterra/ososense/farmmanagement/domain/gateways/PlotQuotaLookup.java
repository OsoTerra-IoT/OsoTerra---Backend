package com.osoterra.ososense.farmmanagement.domain.gateways;

import java.util.Optional;

/**
 * Tells how many active plots an owner may keep under their subscription plan; an owner without
 * an active subscription gets the free plan's limit. Empty only when no limit can be found, in
 * which case none is enforced.
 * Implemented by an anti-corruption layer client into Subscription and Billing.
 */
public interface PlotQuotaLookup {

    Optional<Integer> findPlotLimitForOwner(Long ownerId);
}
