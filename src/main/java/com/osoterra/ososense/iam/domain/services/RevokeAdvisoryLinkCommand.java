package com.osoterra.ososense.iam.domain.services;

public record RevokeAdvisoryLinkCommand(Long linkId, Long requestingUserId) {
}
