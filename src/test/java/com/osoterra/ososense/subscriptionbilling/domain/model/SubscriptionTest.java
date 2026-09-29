package com.osoterra.ososense.subscriptionbilling.domain.model;

import com.osoterra.ososense.shared.BusinessRuleViolationException;
import com.osoterra.ososense.subscriptionbilling.domain.events.SubscriptionActivatedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubscriptionTest {

    private static SubscriptionPlan paidPlan() {
        return SubscriptionPlan.register("Basic", new BigDecimal("29.90"), "PEN", BillingCycle.MONTHLY, 10, false);
    }

    private static SubscriptionPlan freePlan() {
        return SubscriptionPlan.register("Free", BigDecimal.ZERO, "PEN", BillingCycle.NONE, 2, true);
    }

    @Test
    void requestOnAPaidPlanStartsPendingPayment() {
        Subscription subscription = Subscription.request(1L, paidPlan());

        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.PENDING_PAYMENT);
        assertThat(subscription.getQuotaTotal()).isEqualTo(10);
        assertThat(subscription.getPeriodEndDate()).isEmpty();
    }

    @Test
    void requestOnAFreePlanActivatesImmediatelyAndPublishesSubscriptionActivatedEvent() {
        Subscription subscription = Subscription.request(1L, freePlan());

        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        var events = subscription.pullDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOf(SubscriptionActivatedEvent.class);
    }

    @Test
    void activateRejectsASubscriptionThatIsNotPendingPayment() {
        Subscription subscription = Subscription.request(1L, freePlan());

        assertThatThrownBy(() -> subscription.activate(BillingCycle.NONE))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void suspendRejectsASubscriptionThatIsNotActive() {
        Subscription subscription = Subscription.request(1L, paidPlan());

        assertThatThrownBy(subscription::suspend).isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void consumeQuotaRejectsWhenNoQuotaRemains() {
        Subscription subscription = Subscription.request(1L, freePlan());
        subscription.consumeQuota();
        subscription.consumeQuota();

        assertThatThrownBy(subscription::consumeQuota).isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    void cancelRejectsAnAlreadyCancelledSubscription() {
        Subscription subscription = Subscription.request(1L, freePlan());
        subscription.cancel();

        assertThatThrownBy(subscription::cancel).isInstanceOf(BusinessRuleViolationException.class);
    }
}
