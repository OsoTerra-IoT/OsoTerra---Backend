package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransaction;
import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransactionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;

final class PaymentTransactionMapper {

    private PaymentTransactionMapper() {
    }

    static PaymentTransaction toDomain(PaymentTransactionJpaEntity entity) {
        return PaymentTransaction.reconstruct(
                new PaymentTransactionId(entity.getId()),
                new SubscriptionId(entity.getSubscriptionId()),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getExternalReference(),
                entity.isSuccessful(),
                entity.getProcessedAt());
    }

    static PaymentTransactionJpaEntity toEntity(PaymentTransaction transaction) {
        Long id = transaction.getId() == null ? null : transaction.getId().value();
        return new PaymentTransactionJpaEntity(
                id,
                transaction.getSubscriptionId().value(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getExternalReference(),
                transaction.isSuccessful(),
                transaction.getProcessedAt());
    }
}
