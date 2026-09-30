package com.osoterra.ososense.soilmonitoring.domain.events;

import com.osoterra.ososense.shared.domain.events.DomainEvent;
import com.osoterra.ososense.soilmonitoring.domain.model.SoilReadingId;

import java.math.BigDecimal;
import java.time.Instant;

public record SoilReadingStoredEvent(
        SoilReadingId soilReadingId, Long plotId, BigDecimal compensatedConductivityDsM, Instant occurredOn)
        implements DomainEvent {
}
