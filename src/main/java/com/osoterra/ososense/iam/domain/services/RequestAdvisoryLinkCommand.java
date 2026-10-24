package com.osoterra.ososense.iam.domain.services;

/**
 * An advisor asks to supervise a farmer, who is identified by account id or by email.
 */
public record RequestAdvisoryLinkCommand(Long advisorId, Long farmerId, String farmerEmail) {

    public RequestAdvisoryLinkCommand {
        if (farmerId == null && (farmerEmail == null || farmerEmail.isBlank())) {
            throw new IllegalArgumentException("The farmer's id or email is required");
        }
    }
}
