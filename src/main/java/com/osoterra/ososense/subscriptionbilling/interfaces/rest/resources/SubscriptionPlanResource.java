package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import java.math.BigDecimal;

public record SubscriptionPlanResource(
        Long id, String name, BigDecimal priceAmount, String priceCurrency, String billingCycle, int maxPlots,
        boolean free, boolean active) {
}
