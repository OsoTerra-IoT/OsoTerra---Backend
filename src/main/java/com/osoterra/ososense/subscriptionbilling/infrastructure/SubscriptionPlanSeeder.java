package com.osoterra.ososense.subscriptionbilling.infrastructure;

import com.osoterra.ososense.subscriptionbilling.domain.model.BillingCycle;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Preloads a starter catalog of subscription plans so the platform has something to
 * offer before the team defines real commercial pricing. The amounts below are
 * placeholders (PEN) and should be replaced once pricing is finalized. Idempotent:
 * skips any plan that already exists.
 */
@Component
public class SubscriptionPlanSeeder implements ApplicationRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanSeeder(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seed("Free", BigDecimal.ZERO, BillingCycle.NONE, 2, true);
        seed("Basic", new BigDecimal("29.90"), BillingCycle.MONTHLY, 10, false);
        seed("Pro", new BigDecimal("299.90"), BillingCycle.ANNUAL, 50, false);
    }

    private void seed(String name, BigDecimal priceAmount, BillingCycle billingCycle, int maxPlots, boolean isFree) {
        if (!subscriptionPlanRepository.existsByName(name)) {
            subscriptionPlanRepository.save(
                    SubscriptionPlan.register(name, priceAmount, "PEN", billingCycle, maxPlots, isFree));
        }
    }
}
