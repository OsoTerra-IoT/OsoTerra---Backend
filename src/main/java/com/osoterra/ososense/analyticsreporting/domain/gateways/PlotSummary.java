package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.math.BigDecimal;

public record PlotSummary(Long plotId, Long farmId, String plotName, BigDecimal areaHectares, String cropName) {
}
