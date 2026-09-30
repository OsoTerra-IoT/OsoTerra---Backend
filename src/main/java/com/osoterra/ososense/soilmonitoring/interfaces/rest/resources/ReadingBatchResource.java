package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReadingBatchResource(
        Long id, Long deviceId, String status, LocalDateTime submittedAt, int acceptedCount, int discardedCount) {
}
