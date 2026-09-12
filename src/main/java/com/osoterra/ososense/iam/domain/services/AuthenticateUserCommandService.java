package com.osoterra.ososense.iam.domain.services;

public interface AuthenticateUserCommandService {

    AuthenticationResult handle(AuthenticateUserCommand command);
}
