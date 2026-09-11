package com.osoterra.ososense.identityaccess.domain.services;

public interface AuthenticateUserCommandService {

    AuthenticationResult handle(AuthenticateUserCommand command);
}
