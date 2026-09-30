package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;

final class SalinityAlertMapper {

    private SalinityAlertMapper() {
    }

    static SalinityAlert toDomain(SalinityAlertJpaEntity entity) {
        return SalinityAlert.reconstruct(
                new SalinityAlertId(entity.getId()),
                entity.getPlotId(),
                entity.getSoilReadingId(),
                entity.getObservedConductivityDsM(),
                entity.getAppliedThresholdDsM(),
                entity.getExcessRatio(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getAcknowledgedBy(),
                entity.getAcknowledgedAt(),
                entity.getGeneratedAt());
    }

    static SalinityAlertJpaEntity toEntity(SalinityAlert alert) {
        Long id = alert.getId() == null ? null : alert.getId().value();
        return new SalinityAlertJpaEntity(
                id, alert.getPlotId(), alert.getSoilReadingId(), alert.getObservedConductivityDsM(),
                alert.getAppliedThresholdDsM(), alert.getExcessRatio(), alert.getSeverity(), alert.getStatus(),
                alert.getAcknowledgedBy().orElse(null), alert.getAcknowledgedAt().orElse(null), alert.getGeneratedAt());
    }
}
