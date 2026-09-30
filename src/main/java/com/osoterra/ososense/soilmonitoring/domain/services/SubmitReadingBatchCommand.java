package com.osoterra.ososense.soilmonitoring.domain.services;

import java.util.List;

public record SubmitReadingBatchCommand(Long deviceId, List<ReadingPayload> readings) {
}
