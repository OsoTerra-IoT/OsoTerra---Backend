package com.osoterra.ososense.salinityalerting.domain.events;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.shared.DomainEvent;

import java.time.Instant;

public record AlertGeneratedEvent(SalinityAlertId alertId, Long plotId, AlertSeverity severity, Instant occurredOn)
        implements DomainEvent {
}
