package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransaction;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.PaymentTransactionRepository;
import org.springframework.stereotype.Repository;

@Repository
class JpaPaymentTransactionRepository implements PaymentTransactionRepository {

    private final SpringDataPaymentTransactionJpaRepository springDataRepository;

    JpaPaymentTransactionRepository(SpringDataPaymentTransactionJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public PaymentTransaction save(PaymentTransaction transaction) {
        PaymentTransactionJpaEntity saved = springDataRepository.save(PaymentTransactionMapper.toEntity(transaction));
        return PaymentTransactionMapper.toDomain(saved);
    }

    @Override
    public boolean existsByExternalReference(String externalReference) {
        return springDataRepository.existsByExternalReference(externalReference);
    }
}
