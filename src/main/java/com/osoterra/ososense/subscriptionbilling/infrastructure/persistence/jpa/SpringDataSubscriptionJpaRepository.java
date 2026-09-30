package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

interface SpringDataSubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

    List<SubscriptionJpaEntity> findByUserAccountId(Long userAccountId);

    List<SubscriptionJpaEntity> findByStatusAndPeriodEndDateBefore(SubscriptionStatus status, LocalDate date);
}
