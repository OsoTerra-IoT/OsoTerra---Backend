package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterCorrectiveActionResource(
        @NotBlank String actionType, @NotNull LocalDate executedAt, String notes) {
}
