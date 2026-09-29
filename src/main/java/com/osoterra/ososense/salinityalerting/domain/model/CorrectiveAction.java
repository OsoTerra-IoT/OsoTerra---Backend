package com.osoterra.ososense.salinityalerting.domain.model;

import com.osoterra.ososense.salinityalerting.domain.events.CorrectiveActionRegisteredEvent;
import com.osoterra.ososense.shared.AggregateRoot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for the corrective action a producer registers in response to a
 * salinity alert. One alert has at most one corrective action.
 */
public final class CorrectiveAction extends AggregateRoot<CorrectiveActionId> {

    private final Long salinityAlertId;
    private final CorrectiveActionType actionType;
    private final LocalDate executedAt;
    private final String notes;
    private final Long registeredBy;
    private final LocalDateTime registeredAt;

    private CorrectiveAction(
            CorrectiveActionId id, Long salinityAlertId, CorrectiveActionType actionType, LocalDate executedAt,
            String notes, Long registeredBy, LocalDateTime registeredAt) {
        super(id);
        this.salinityAlertId = salinityAlertId;
        this.actionType = actionType;
        this.executedAt = executedAt;
        this.notes = notes;
        this.registeredBy = registeredBy;
        this.registeredAt = registeredAt;
    }

    public static CorrectiveAction register(
            Long salinityAlertId, CorrectiveActionType actionType, LocalDate executedAt, String notes,
            Long registeredBy) {
        Objects.requireNonNull(salinityAlertId, "salinityAlertId");
        Objects.requireNonNull(actionType, "actionType");
        Objects.requireNonNull(executedAt, "executedAt");
        Objects.requireNonNull(registeredBy, "registeredBy");
        CorrectiveAction action = new CorrectiveAction(
                null, salinityAlertId, actionType, executedAt, notes, registeredBy, LocalDateTime.now());
        action.registerEvent(new CorrectiveActionRegisteredEvent(action.getId(), salinityAlertId, Instant.now()));
        return action;
    }

    public static CorrectiveAction reconstruct(
            CorrectiveActionId id, Long salinityAlertId, CorrectiveActionType actionType, LocalDate executedAt,
            String notes, Long registeredBy, LocalDateTime registeredAt) {
        return new CorrectiveAction(id, salinityAlertId, actionType, executedAt, notes, registeredBy, registeredAt);
    }

    public Long getSalinityAlertId() {
        return salinityAlertId;
    }

    public CorrectiveActionType getActionType() {
        return actionType;
    }

    public LocalDate getExecutedAt() {
        return executedAt;
    }

    public Optional<String> getNotes() {
        return Optional.ofNullable(notes);
    }

    public Long getRegisteredBy() {
        return registeredBy;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
}
