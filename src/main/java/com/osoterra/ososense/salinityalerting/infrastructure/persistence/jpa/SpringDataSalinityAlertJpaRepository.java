package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SpringDataSalinityAlertJpaRepository extends JpaRepository<SalinityAlertJpaEntity, Long> {

    List<SalinityAlertJpaEntity> findByPlotId(Long plotId);

    boolean existsByPlotIdAndSeverityAndStatus(Long plotId, AlertSeverity severity, AlertStatus status);
}
