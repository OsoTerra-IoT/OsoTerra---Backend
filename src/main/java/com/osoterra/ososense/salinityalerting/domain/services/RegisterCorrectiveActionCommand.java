package com.osoterra.ososense.salinityalerting.domain.services;

import java.time.LocalDate;

public record RegisterCorrectiveActionCommand(
        Long salinityAlertId, String actionType, LocalDate executedAt, String notes, Long registeredBy) {
}
