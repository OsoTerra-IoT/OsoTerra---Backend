package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataCorrectiveActionJpaRepository extends JpaRepository<CorrectiveActionJpaEntity, Long> {

    Optional<CorrectiveActionJpaEntity> findBySalinityAlertId(Long salinityAlertId);
}
