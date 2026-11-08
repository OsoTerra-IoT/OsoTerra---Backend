package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionResourceAssembler {

    /**
     * {@code quotaConsumed} is the number of active plots the account holder really has, not
     * the counter stored on the subscription, which nothing updates.
     */
    public SubscriptionResource toResource(Subscription subscription, int quotaConsumed) {
        return new SubscriptionResource(
                subscription.getId().value(),
                subscription.getUserAccountId(),
                subscription.getSubscriptionPlanId().value(),
                subscription.getStatus().name(),
                subscription.getQuotaTotal(),
                quotaConsumed,
                subscription.getPeriodStartDate(),
                subscription.getPeriodEndDate().orElse(null),
                subscription.getCreatedAt());
    }
}
