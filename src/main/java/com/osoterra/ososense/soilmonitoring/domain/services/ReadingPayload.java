package com.osoterra.ososense.soilmonitoring.domain.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * One sensor reading within a submitted batch. Raw and compensated values, and the
 * compensation factor applied, all come from the Edge Service — this codebase does not
 * recompute the compensation formula.
 */
public record ReadingPayload(
        BigDecimal rawConductivityDsM,
        BigDecimal compensatedConductivityDsM,
        BigDecimal compensationFactor,
        BigDecimal moisturePercentage,
        BigDecimal temperatureCelsius,
        LocalDateTime capturedAt) {
}
