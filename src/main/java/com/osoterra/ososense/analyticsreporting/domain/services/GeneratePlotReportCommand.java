package com.osoterra.ososense.analyticsreporting.domain.services;

import java.time.LocalDate;

public record GeneratePlotReportCommand(Long plotId, Long generatedBy, LocalDate periodStart, LocalDate periodEnd) {
}
