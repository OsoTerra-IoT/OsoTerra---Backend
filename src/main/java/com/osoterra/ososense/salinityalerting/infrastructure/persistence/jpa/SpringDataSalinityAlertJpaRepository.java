package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SpringDataSalinityAlertJpaRepository extends JpaRepository<SalinityAlertJpaEntity, Long> {

    List<SalinityAlertJpaEntity> findByPlotId(Long plotId);

    Optional<SalinityAlertJpaEntity> findFirstByPlotIdOrderByGeneratedAtDescIdDesc(Long plotId);
}
