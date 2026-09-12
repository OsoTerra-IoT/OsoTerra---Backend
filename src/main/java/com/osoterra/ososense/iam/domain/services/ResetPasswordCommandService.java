package com.osoterra.ososense.iam.domain.services;

public interface ResetPasswordCommandService {

    void handle(ResetPasswordCommand command);
}
