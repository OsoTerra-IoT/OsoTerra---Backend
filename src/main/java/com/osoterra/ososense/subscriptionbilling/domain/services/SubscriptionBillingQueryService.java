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
}
