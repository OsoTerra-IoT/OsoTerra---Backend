package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import com.osoterra.ososense.soilmonitoring.domain.model.ReadingBatch;
import org.springframework.stereotype.Component;

@Component
public class ReadingBatchResourceAssembler {

    public ReadingBatchResource toResource(ReadingBatch batch) {
        return new ReadingBatchResource(
                batch.getId().value(),
                batch.getDeviceId(),
                batch.getStatus().name(),
                batch.getSubmittedAt(),
                batch.getAcceptedCount(),
                batch.getDiscardedCount());
    }
}
