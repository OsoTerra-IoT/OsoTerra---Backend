package com.osoterra.ososense.soilmonitoring.infrastructure.persistence.jpa;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchId;

final class ReadingBatchMapper {

    private ReadingBatchMapper() {
    }

    static ReadingBatch toDomain(ReadingBatchJpaEntity entity) {
        return ReadingBatch.reconstruct(
                new ReadingBatchId(entity.getId()),
                entity.getDeviceId(),
                entity.getStatus(),
                entity.getSubmittedAt(),
                entity.getAcceptedCount(),
                entity.getDiscardedCount());
    }

    static ReadingBatchJpaEntity toEntity(ReadingBatch batch) {
        Long id = batch.getId() == null ? null : batch.getId().value();
        return new ReadingBatchJpaEntity(
                id, batch.getDeviceId(), batch.getStatus(), batch.getSubmittedAt(), batch.getAcceptedCount(),
                batch.getDiscardedCount());
    }
}
