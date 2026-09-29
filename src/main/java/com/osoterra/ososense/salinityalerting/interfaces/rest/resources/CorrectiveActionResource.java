package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CorrectiveActionResource(
        Long id, Long salinityAlertId, String actionType, LocalDate executedAt, String notes, Long registeredBy,
        LocalDateTime registeredAt) {
}
