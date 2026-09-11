package com.osoterra.ososense.identityaccess.domain.services;

public record RevokeAdvisoryLinkCommand(Long linkId, Long requestingUserId) {
}
