package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlotResource(
        Long id, Long farmId, Long cropId, String name, BigDecimal areaHectares, BigDecimal latitude,
        BigDecimal longitude, boolean active, LocalDateTime createdAt) {
}
