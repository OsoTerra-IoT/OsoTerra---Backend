package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;

final class SubscriptionMapper {

    private SubscriptionMapper() {
    }

    static Subscription toDomain(SubscriptionJpaEntity entity) {
        return Subscription.reconstruct(
                new SubscriptionId(entity.getId()),
                entity.getUserAccountId(),
                new SubscriptionPlanId(entity.getSubscriptionPlanId()),
                entity.getStatus(),
                entity.getQuotaTotal(),
                entity.getQuotaConsumed(),
                entity.getPeriodStartDate(),
                entity.getPeriodEndDate(),
                entity.getCreatedAt());
    }

    static SubscriptionJpaEntity toEntity(Subscription subscription) {
        Long id = subscription.getId() == null ? null : subscription.getId().value();
        return new SubscriptionJpaEntity(
                id,
                subscription.getUserAccountId(),
                subscription.getSubscriptionPlanId().value(),
                subscription.getStatus(),
                subscription.getQuotaTotal(),
                subscription.getQuotaConsumed(),
                subscription.getPeriodStartDate(),
                subscription.getPeriodEndDate().orElse(null),
                subscription.getCreatedAt());
    }
}
