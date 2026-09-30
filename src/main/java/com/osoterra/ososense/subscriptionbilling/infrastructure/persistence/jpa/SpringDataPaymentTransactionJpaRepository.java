package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPaymentTransactionJpaRepository extends JpaRepository<PaymentTransactionJpaEntity, Long> {

    boolean existsByExternalReference(String externalReference);
}
