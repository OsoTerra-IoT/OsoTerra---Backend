package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataSubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlanJpaEntity, Long> {

    boolean existsByName(String name);

    List<SubscriptionPlanJpaEntity> findByActiveTrue();
}
