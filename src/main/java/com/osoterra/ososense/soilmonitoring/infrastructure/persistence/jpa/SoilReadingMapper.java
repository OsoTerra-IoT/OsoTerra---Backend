package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchId;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReading;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReadingId;

final class SoilReadingMapper {

    private SoilReadingMapper() {
    }

    static SoilReading toDomain(SoilReadingJpaEntity entity) {
        ReadingBatchId readingBatchId = entity.getReadingBatchId() == null
                ? null
                : new ReadingBatchId(entity.getReadingBatchId());
        return SoilReading.reconstruct(
                new SoilReadingId(entity.getId()),
                entity.getDeviceId(),
                entity.getPlotId(),
                readingBatchId,
                entity.getRawConductivityDsM(),
                entity.getCompensatedConductivityDsM(),
                entity.getCompensationFactor(),
                entity.getMoisturePercentage(),
                entity.getTemperatureCelsius(),
                entity.getCapturedAt(),
                entity.getStoredAt());
    }

    static SoilReadingJpaEntity toEntity(SoilReading reading) {
        Long id = reading.getId() == null ? null : reading.getId().value();
        Long readingBatchId = reading.getReadingBatchId().map(ReadingBatchId::value).orElse(null);
        return new SoilReadingJpaEntity(
                id, reading.getDeviceId(), reading.getPlotId(), readingBatchId, reading.getRawConductivityDsM(),
                reading.getCompensatedConductivityDsM(), reading.getCompensationFactor(),
                reading.getMoisturePercentage(), reading.getTemperatureCelsius(), reading.getCapturedAt(),
                reading.getStoredAt());
    }
}
