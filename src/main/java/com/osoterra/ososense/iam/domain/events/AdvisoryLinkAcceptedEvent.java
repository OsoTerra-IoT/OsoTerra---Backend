package com.osoterra.ososense.iam.domain.events;

import com.osoterra.ososense.iam.domain.model.AdvisoryLinkId;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.shared.domain.events.DomainEvent;

import java.time.Instant;

public record AdvisoryLinkAcceptedEvent(
        AdvisoryLinkId linkId,
        UserAccountId advisorId,
        UserAccountId farmerId,
        Instant occurredOn) implements DomainEvent {
}
