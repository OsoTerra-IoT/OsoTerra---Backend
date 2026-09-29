package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionResourceAssembler {

    public SubscriptionResource toResource(Subscription subscription) {
        return new SubscriptionResource(
                subscription.getId().value(),
                subscription.getUserAccountId(),
                subscription.getSubscriptionPlanId().value(),
                subscription.getStatus().name(),
                subscription.getQuotaTotal(),
                subscription.getQuotaConsumed(),
                subscription.getPeriodStartDate(),
                subscription.getPeriodEndDate().orElse(null),
                subscription.getCreatedAt());
    }
}
