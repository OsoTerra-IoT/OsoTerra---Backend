package com.osoterra.ososense.identityaccess.domain.services;

public interface RequestPasswordResetCommandService {

    /**
     * Issues a reset token and notifies the owner when the email belongs to an active
     * account. Does nothing otherwise, without revealing whether the email is registered.
     */
    void handle(RequestPasswordResetCommand command);
}
