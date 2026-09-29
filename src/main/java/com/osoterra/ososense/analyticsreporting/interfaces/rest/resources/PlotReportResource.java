package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PlotReportResource(
        Long id, Long plotId, Long generatedBy, LocalDate periodStartDate, LocalDate periodEndDate,
        LocalDateTime generatedAt) {
}
