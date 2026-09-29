package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataNotificationPreferenceJpaRepository extends JpaRepository<NotificationPreferenceJpaEntity, Long> {

    Optional<NotificationPreferenceJpaEntity> findByUserAccountId(Long userAccountId);
}
