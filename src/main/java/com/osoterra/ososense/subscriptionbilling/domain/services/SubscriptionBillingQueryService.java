package com.osoterra.ososense.subscriptionbilling.domain.services;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;

import java.util.List;
import java.util.Optional;

public interface SubscriptionBillingQueryService {

    List<SubscriptionPlan> findActivePlans();

    Optional<SubscriptionPlan> findPlanById(SubscriptionPlanId id);

    Optional<Subscription> findSubscriptionById(SubscriptionId id);

    List<Subscription> findSubscriptionsByUserAccountId(Long userAccountId);

    /**
     * The plot limit of the account's active subscription (the largest one when several are
     * active), or empty when the account has no active subscription.
     */
    Optional<Integer> findPlotLimitByUserAccountId(Long userAccountId);

    /**
     * How much of the plot quota the account really uses: its active plots, counted when asked.
     */
    int findConsumedQuotaByUserAccountId(Long userAccountId);
}
