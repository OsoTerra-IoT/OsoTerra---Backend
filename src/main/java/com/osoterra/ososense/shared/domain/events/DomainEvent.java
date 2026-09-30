package com.osoterra.ososense.shared.domain.events;

import java.time.Instant;

/**
 * A fact that occurred inside an aggregate and is relevant to other parts of the domain.
 */
public interface DomainEvent {
    Instant occurredOn();
}
