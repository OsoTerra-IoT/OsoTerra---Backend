package com.osoterra.ososense.subscriptionbilling.domain.repositories;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository {

    SubscriptionPlan save(SubscriptionPlan plan);

    Optional<SubscriptionPlan> findById(SubscriptionPlanId id);

    boolean existsByName(String name);

    List<SubscriptionPlan> findByIsActiveTrue();
}
