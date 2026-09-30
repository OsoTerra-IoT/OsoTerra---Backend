package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ComputeSalinityTrendResource(@NotNull Long plotId, @NotNull LocalDate periodStart, @NotNull LocalDate periodEnd) {
}
