package com.osoterra.ososense.salinityalerting.domain.events;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveActionId;
import com.osoterra.ososense.shared.DomainEvent;

import java.time.Instant;

public record CorrectiveActionRegisteredEvent(CorrectiveActionId actionId, Long salinityAlertId, Instant occurredOn)
        implements DomainEvent {
}
