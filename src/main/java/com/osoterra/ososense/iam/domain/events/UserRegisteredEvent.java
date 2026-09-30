package com.osoterra.ososense.iam.domain.events;

import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.UserRole;
import com.osoterra.ososense.shared.domain.events.DomainEvent;

import java.time.Instant;

public record UserRegisteredEvent(EmailAddress email, UserRole role, Instant occurredOn) implements DomainEvent {
}
