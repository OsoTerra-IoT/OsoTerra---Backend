package com.osoterra.ososense.farmmanagement.domain.gateways;

import java.util.Optional;

/**
 * Tells how many active plots an owner may keep under the subscription plan they pay for.
 * Empty when the owner has no active subscription, in which case no limit is enforced.
 * Implemented by an anti-corruption layer client into Subscription and Billing.
 */
public interface PlotQuotaLookup {

    Optional<Integer> findPlotLimitForOwner(Long ownerId);
}
