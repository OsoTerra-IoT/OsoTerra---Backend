package com.osoterra.ososense.salinityalerting.domain.repositories;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.AlertStatus;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;

import java.util.List;
import java.util.Optional;

public interface SalinityAlertRepository {

    SalinityAlert save(SalinityAlert alert);

    Optional<SalinityAlert> findById(SalinityAlertId id);

    List<SalinityAlert> findByPlotId(Long plotId);

    boolean existsByPlotIdAndSeverityAndStatus(Long plotId, AlertSeverity severity, AlertStatus status);
}
