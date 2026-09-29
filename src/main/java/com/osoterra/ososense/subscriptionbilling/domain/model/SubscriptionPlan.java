package com.osoterra.ososense.subscriptionbilling.domain.model;

import com.osoterra.ososense.shared.AggregateRoot;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Aggregate root for a subscription plan in the public catalog. Reference data
 * preloaded by {@code SubscriptionPlanSeeder}.
 */
public final class SubscriptionPlan extends AggregateRoot<SubscriptionPlanId> {

    private final String name;
    private final BigDecimal priceAmount;
    private final String priceCurrency;
    private final BillingCycle billingCycle;
    private final int maxPlots;
    private final boolean isFree;
    private boolean isActive;

    private SubscriptionPlan(
            SubscriptionPlanId id, String name, BigDecimal priceAmount, String priceCurrency,
            BillingCycle billingCycle, int maxPlots, boolean isFree, boolean isActive) {
        super(id);
        this.name = name;
        this.priceAmount = priceAmount;
        this.priceCurrency = priceCurrency;
        this.billingCycle = billingCycle;
        this.maxPlots = maxPlots;
        this.isFree = isFree;
        this.isActive = isActive;
    }

    public static SubscriptionPlan register(
            String name, BigDecimal priceAmount, String priceCurrency, BillingCycle billingCycle, int maxPlots,
            boolean isFree) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(priceAmount, "priceAmount");
        Objects.requireNonNull(billingCycle, "billingCycle");
        if (name.isBlank()) {
            throw new IllegalArgumentException("A subscription plan's name must not be blank");
        }
        if (maxPlots <= 0) {
            throw new IllegalArgumentException("A subscription plan's max plots must be greater than zero");
        }
        return new SubscriptionPlan(null, name, priceAmount, priceCurrency, billingCycle, maxPlots, isFree, true);
    }

    public static SubscriptionPlan reconstruct(
            SubscriptionPlanId id, String name, BigDecimal priceAmount, String priceCurrency,
            BillingCycle billingCycle, int maxPlots, boolean isFree, boolean isActive) {
        return new SubscriptionPlan(id, name, priceAmount, priceCurrency, billingCycle, maxPlots, isFree, isActive);
    }

    public void deactivate() {
        this.isActive = false;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public int getMaxPlots() {
        return maxPlots;
    }

    public boolean isFree() {
        return isFree;
    }

    public boolean isActive() {
        return isActive;
    }
}
