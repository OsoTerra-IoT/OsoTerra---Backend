package com.osoterra.ososense.subscriptionbilling.domain.events;

import com.osoterra.ososense.shared.domain.events.DomainEvent;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;

import java.time.Instant;

public record SubscriptionSuspendedEvent(SubscriptionId subscriptionId, Long userAccountId, Instant occurredOn)
        implements DomainEvent {
}
