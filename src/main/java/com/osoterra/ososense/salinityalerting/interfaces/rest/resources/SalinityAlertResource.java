package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SalinityAlertResource(
        Long id, Long plotId, Long soilReadingId, BigDecimal observedConductivityDsM, BigDecimal appliedThresholdDsM,
        BigDecimal excessRatio, String severity, String status, Long acknowledgedBy, LocalDateTime acknowledgedAt,
        LocalDateTime generatedAt) {
}
