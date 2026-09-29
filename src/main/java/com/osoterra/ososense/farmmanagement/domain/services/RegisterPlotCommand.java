package com.osoterra.ososense.farmmanagement.domain.services;

import java.math.BigDecimal;

public record RegisterPlotCommand(
        Long farmId, String name, BigDecimal areaHectares, BigDecimal latitude, BigDecimal longitude) {
}
