package com.osoterra.ososense.farmmanagement.domain.services;

import java.math.BigDecimal;

public record UpdatePlotCommand(
        Long plotId, Long requestedBy, String name, BigDecimal areaHectares, BigDecimal latitude,
        BigDecimal longitude) {
}
