package com.osoterra.ososense.subscriptionbilling.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;
import com.osoterra.ososense.subscriptionbilling.domain.events.SubscriptionActivatedEvent;
import com.osoterra.ososense.subscriptionbilling.domain.events.SubscriptionSuspendedEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a user's subscription. The quota is copied from the plan at
 * request time and never re-read from the catalog afterward, so a later change to the
 * plan's price or conditions does not retroactively alter subscriptions already active.
 */
public final class Subscription extends AggregateRoot<SubscriptionId> {

    private final Long userAccountId;
    private final SubscriptionPlanId subscriptionPlanId;
    private SubscriptionStatus status;
    private final int quotaTotal;
    private int quotaConsumed;
    private final LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private final LocalDateTime createdAt;

    private Subscription(
            SubscriptionId id, Long userAccountId, SubscriptionPlanId subscriptionPlanId, SubscriptionStatus status,
            int quotaTotal, int quotaConsumed, LocalDate periodStartDate, LocalDate periodEndDate,
            LocalDateTime createdAt) {
        super(id);
        this.userAccountId = userAccountId;
        this.subscriptionPlanId = subscriptionPlanId;
        this.status = status;
        this.quotaTotal = quotaTotal;
        this.quotaConsumed = quotaConsumed;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.createdAt = createdAt;
    }

    /**
     * Requests a subscription to the given plan. Free plans activate immediately;
     * paid plans stay {@link SubscriptionStatus#PENDING_PAYMENT} until a successful
     * payment is recorded.
     */
    public static Subscription request(Long userAccountId, SubscriptionPlan plan) {
        Objects.requireNonNull(userAccountId, "userAccountId");
        Objects.requireNonNull(plan, "plan");
        Subscription subscription = new Subscription(
                null, userAccountId, plan.getId(), SubscriptionStatus.PENDING_PAYMENT, plan.getMaxPlots(), 0,
                LocalDate.now(), null, LocalDateTime.now());
        if (plan.isFree()) {
            subscription.activate(plan.getBillingCycle());
        }
        return subscription;
    }

    public static Subscription reconstruct(
            SubscriptionId id, Long userAccountId, SubscriptionPlanId subscriptionPlanId, SubscriptionStatus status,
            int quotaTotal, int quotaConsumed, LocalDate periodStartDate, LocalDate periodEndDate,
            LocalDateTime createdAt) {
        return new Subscription(
                id, userAccountId, subscriptionPlanId, status, quotaTotal, quotaConsumed, periodStartDate,
                periodEndDate, createdAt);
    }

    public void activate(BillingCycle billingCycle) {
        if (status != SubscriptionStatus.PENDING_PAYMENT) {
            throw new BusinessRuleViolationException("Only a subscription pending payment can be activated");
        }
        this.status = SubscriptionStatus.ACTIVE;
        this.periodEndDate = switch (billingCycle) {
            case MONTHLY -> periodStartDate.plusMonths(1);
            case ANNUAL -> periodStartDate.plusYears(1);
            case NONE -> null;
        };
        registerEvent(new SubscriptionActivatedEvent(getId(), userAccountId, Instant.now()));
    }

    public void suspend() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleViolationException("Only an active subscription can be suspended");
        }
        this.status = SubscriptionStatus.SUSPENDED;
        registerEvent(new SubscriptionSuspendedEvent(getId(), userAccountId, Instant.now()));
    }

    public void cancel() {
        if (status == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleViolationException("The subscription is already cancelled");
        }
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void consumeQuota() {
        if (quotaConsumed >= quotaTotal) {
            throw new BusinessRuleViolationException("No plot quota remaining on this subscription");
        }
        this.quotaConsumed++;
    }

    public void releaseQuota() {
        if (quotaConsumed > 0) {
            this.quotaConsumed--;
        }
    }

    public Long getUserAccountId() {
        return userAccountId;
    }

    public SubscriptionPlanId getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public int getQuotaTotal() {
        return quotaTotal;
    }

    public int getQuotaConsumed() {
        return quotaConsumed;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public Optional<LocalDate> getPeriodEndDate() {
        return Optional.ofNullable(periodEndDate);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
