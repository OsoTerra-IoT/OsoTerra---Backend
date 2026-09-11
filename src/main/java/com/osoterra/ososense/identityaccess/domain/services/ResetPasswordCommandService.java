package com.osoterra.ososense.identityaccess.domain.services;

public interface ResetPasswordCommandService {

    void handle(ResetPasswordCommand command);
}
