package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeviceResource(
        Long id, Long plotId, String activationCode, String status, BigDecimal calibrationFactor,
        Integer batteryLevel, String firmwareVersion, int readingIntervalMinutes, LocalDateTime lastSeenAt,
        LocalDateTime createdAt) {
}
