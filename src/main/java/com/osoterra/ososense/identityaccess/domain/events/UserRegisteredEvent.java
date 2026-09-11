package com.osoterra.ososense.identityaccess.domain.events;

import com.osoterra.ososense.identityaccess.domain.model.EmailAddress;
import com.osoterra.ososense.identityaccess.domain.model.UserRole;
import com.osoterra.ososense.shared.DomainEvent;

import java.time.Instant;

public record UserRegisteredEvent(EmailAddress email, UserRole role, Instant occurredOn) implements DomainEvent {
}
