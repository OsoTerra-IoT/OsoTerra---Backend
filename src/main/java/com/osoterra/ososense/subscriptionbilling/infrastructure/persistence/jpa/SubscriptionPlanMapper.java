package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;

final class SubscriptionPlanMapper {

    private SubscriptionPlanMapper() {
    }

    static SubscriptionPlan toDomain(SubscriptionPlanJpaEntity entity) {
        return SubscriptionPlan.reconstruct(
                new SubscriptionPlanId(entity.getId()),
                entity.getName(),
                entity.getPriceAmount(),
                entity.getPriceCurrency(),
                entity.getBillingCycle(),
                entity.getMaxPlots(),
                entity.isFree(),
                entity.isActive());
    }

    static SubscriptionPlanJpaEntity toEntity(SubscriptionPlan plan) {
        Long id = plan.getId() == null ? null : plan.getId().value();
        return new SubscriptionPlanJpaEntity(
                id,
                plan.getName(),
                plan.getPriceAmount(),
                plan.getPriceCurrency(),
                plan.getBillingCycle(),
                plan.getMaxPlots(),
                plan.isFree(),
                plan.isActive());
    }
}
