package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AdvisoryLinkResource(
        Long id,
        Long advisorId,
        Long farmerId,
        String status,
        LocalDateTime requestedAt,
        LocalDateTime respondedAt) {
}
