package com.osoterra.ososense.soilmonitoring.domain.repositories;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatchId;

import java.util.Optional;

public interface ReadingBatchRepository {

    ReadingBatch save(ReadingBatch batch);

    Optional<ReadingBatch> findById(ReadingBatchId id);
}
