package com.osoterra.ososense.iam.domain.services;

public interface AuthenticateWithGoogleCommandService {

    AuthenticationResult handle(AuthenticateWithGoogleCommand command);
}
