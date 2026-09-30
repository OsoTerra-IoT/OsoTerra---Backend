package com.osoterra.ososense.soilmonitoring.domain.services;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;

public interface SubmitReadingBatchCommandService {

    ReadingBatch handle(SubmitReadingBatchCommand command);
}
