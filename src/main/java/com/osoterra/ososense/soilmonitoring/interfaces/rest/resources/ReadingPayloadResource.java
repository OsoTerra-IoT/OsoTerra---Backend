package com.osoterra.ososense.soilmonitoring.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReadingPayloadResource(
        @NotNull BigDecimal rawConductivityDsM,
        @NotNull BigDecimal compensatedConductivityDsM,
        @NotNull BigDecimal compensationFactor,
        @NotNull BigDecimal moisturePercentage,
        @NotNull BigDecimal temperatureCelsius,
        @NotNull LocalDateTime capturedAt) {
}
