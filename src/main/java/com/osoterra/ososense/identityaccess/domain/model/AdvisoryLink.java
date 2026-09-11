package com.osoterra.ososense.identityaccess.domain.model;

import com.osoterra.ososense.identityaccess.domain.events.AdvisoryLinkAcceptedEvent;
import com.osoterra.ososense.identityaccess.domain.events.AdvisoryLinkRevokedEvent;
import com.osoterra.ososense.shared.AggregateRoot;
import com.osoterra.ososense.shared.BusinessRuleViolationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Aggregate root for the supervision link requested by an advisor over a farmer. Its
 * lifecycle and consent rules are independent of the two accounts it references.
 */
public final class AdvisoryLink extends AggregateRoot<AdvisoryLinkId> {

    private final UserAccountId advisorId;
    private final UserAccountId farmerId;
    private LinkStatus status;
    private final LocalDateTime requestedAt;
    private LocalDateTime respondedAt;

    private AdvisoryLink(
            AdvisoryLinkId id,
            UserAccountId advisorId,
            UserAccountId farmerId,
            LinkStatus status,
            LocalDateTime requestedAt,
            LocalDateTime respondedAt) {
        super(id);
        this.advisorId = advisorId;
        this.farmerId = farmerId;
        this.status = status;
        this.requestedAt = requestedAt;
        this.respondedAt = respondedAt;
    }

    /**
     * Creates a new link request in {@link LinkStatus#PENDING}, awaiting the farmer's consent.
     */
    public static AdvisoryLink request(UserAccountId advisorId, UserAccountId farmerId) {
        if (advisorId.equals(farmerId)) {
            throw new IllegalArgumentException("An advisor cannot request a link with themselves");
        }
        return new AdvisoryLink(null, advisorId, farmerId, LinkStatus.PENDING, LocalDateTime.now(), null);
    }

    /**
     * Rebuilds a link from persisted data without raising any domain event.
     */
    public static AdvisoryLink reconstruct(
            AdvisoryLinkId id,
            UserAccountId advisorId,
            UserAccountId farmerId,
            LinkStatus status,
            LocalDateTime requestedAt,
            LocalDateTime respondedAt) {
        return new AdvisoryLink(id, advisorId, farmerId, status, requestedAt, respondedAt);
    }

    public void accept() {
        if (status != LinkStatus.PENDING) {
            throw new BusinessRuleViolationException("Only a pending advisory link can be accepted");
        }
        this.status = LinkStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
        registerEvent(new AdvisoryLinkAcceptedEvent(id, advisorId, farmerId, Instant.now()));
    }

    public void revoke() {
        if (status == LinkStatus.REVOKED) {
            throw new BusinessRuleViolationException("The advisory link is already revoked");
        }
        this.status = LinkStatus.REVOKED;
        this.respondedAt = LocalDateTime.now();
        registerEvent(new AdvisoryLinkRevokedEvent(id, advisorId, farmerId, Instant.now()));
    }

    public boolean isActive() {
        return status == LinkStatus.ACCEPTED;
    }

    public UserAccountId getAdvisorId() {
        return advisorId;
    }

    public UserAccountId getFarmerId() {
        return farmerId;
    }

    public LinkStatus getStatus() {
        return status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public Optional<LocalDateTime> getRespondedAt() {
        return Optional.ofNullable(respondedAt);
    }
}
