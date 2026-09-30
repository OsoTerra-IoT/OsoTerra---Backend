package com.osoterra.ososense.analyticsreporting.domain.services;

import java.time.LocalDate;

public record ComputeSalinityTrendCommand(Long plotId, LocalDate periodStart, LocalDate periodEnd) {
}
