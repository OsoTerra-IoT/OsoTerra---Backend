package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SalinityTrendResource(
        Long id, Long plotId, LocalDate periodStartDate, LocalDate periodEndDate, BigDecimal slopeDsMPerDay,
        String direction, int readingCount, LocalDateTime computedAt) {
}
