package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanResourceAssembler {

    public SubscriptionPlanResource toResource(SubscriptionPlan plan) {
        return new SubscriptionPlanResource(
                plan.getId().value(),
                plan.getName(),
                plan.getPriceAmount(),
                plan.getPriceCurrency(),
                plan.getBillingCycle().name(),
                plan.getMaxPlots(),
                plan.isFree(),
                plan.isActive());
    }
}
