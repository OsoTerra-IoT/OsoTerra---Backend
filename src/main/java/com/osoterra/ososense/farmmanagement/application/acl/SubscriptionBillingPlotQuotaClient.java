package com.osoterra.ososense.farmmanagement.application.acl;

import com.osoterra.ososense.farmmanagement.domain.gateways.PlotQuotaLookup;
import com.osoterra.ososense.subscriptionbilling.domain.services.SubscriptionBillingQueryService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anti-corruption layer resolving an owner's plot limit through Subscription and Billing's
 * query service, so Farm Management never handles subscription types.
 */
@Component
class SubscriptionBillingPlotQuotaClient implements PlotQuotaLookup {

    private final SubscriptionBillingQueryService subscriptionBillingQueryService;

    SubscriptionBillingPlotQuotaClient(SubscriptionBillingQueryService subscriptionBillingQueryService) {
        this.subscriptionBillingQueryService = subscriptionBillingQueryService;
    }

    @Override
    public Optional<Integer> findPlotLimitForOwner(Long ownerId) {
        return subscriptionBillingQueryService.findPlotLimitByUserAccountId(ownerId);
    }
}
