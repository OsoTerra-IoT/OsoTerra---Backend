package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.math.BigDecimal;
import java.util.List;

public record PlotDashboardResource(
        Long plotId, String plotName, BigDecimal areaHectares, String cropName,
        List<AlertSummaryResource> recentAlerts) {
}
