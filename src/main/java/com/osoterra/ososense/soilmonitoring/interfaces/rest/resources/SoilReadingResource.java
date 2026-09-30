package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SoilReadingResource(
        Long id, Long deviceId, Long plotId, Long readingBatchId, BigDecimal rawConductivityDsM,
        BigDecimal compensatedConductivityDsM, BigDecimal compensationFactor, BigDecimal moisturePercentage,
        BigDecimal temperatureCelsius, LocalDateTime capturedAt, LocalDateTime storedAt) {
}
