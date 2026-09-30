package com.osoterra.ososense.subscriptionbilling.interfaces.rest.controllers;

import com.osoterra.ososense.subscriptionbilling.domain.services.SubscriptionBillingQueryService;
import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.SubscriptionPlanResource;
import com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources.SubscriptionPlanResourceAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only public catalog of subscription plans.
 */
@RestController
@RequestMapping("/api/v1/subscription-plans")
class SubscriptionPlanController {

    private final SubscriptionBillingQueryService subscriptionBillingQueryService;
    private final SubscriptionPlanResourceAssembler subscriptionPlanResourceAssembler;

    SubscriptionPlanController(
            SubscriptionBillingQueryService subscriptionBillingQueryService,
            SubscriptionPlanResourceAssembler subscriptionPlanResourceAssembler) {
        this.subscriptionBillingQueryService = subscriptionBillingQueryService;
        this.subscriptionPlanResourceAssembler = subscriptionPlanResourceAssembler;
    }

    @GetMapping
    List<SubscriptionPlanResource> all() {
        return subscriptionBillingQueryService.findActivePlans().stream()
                .map(subscriptionPlanResourceAssembler::toResource)
                .toList();
    }
}
