package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecord;
import com.osoterra.ososense.soilmonitoring.domain.model.CalibrationRecordId;

final class CalibrationRecordMapper {

    private CalibrationRecordMapper() {
    }

    static CalibrationRecord toDomain(CalibrationRecordJpaEntity entity) {
        return CalibrationRecord.reconstruct(
                new CalibrationRecordId(entity.getId()),
                entity.getDeviceId(),
                entity.getLabConductivityDsM(),
                entity.getSamplingDate(),
                entity.getLaboratoryName(),
                entity.getDeviceReadingAtSampling(),
                entity.getResultingFactor(),
                entity.getRegisteredAt());
    }

    static CalibrationRecordJpaEntity toEntity(CalibrationRecord record) {
        Long id = record.getId() == null ? null : record.getId().value();
        return new CalibrationRecordJpaEntity(
                id, record.getDeviceId(), record.getLabConductivityDsM(), record.getSamplingDate(),
                record.getLaboratoryName(), record.getDeviceReadingAtSampling(), record.getResultingFactor(),
                record.getRegisteredAt());
    }
}
