package com.osoterra.ososense.identityaccess.domain.services;

import com.osoterra.ososense.identityaccess.domain.model.UserAccount;

public interface RegisterUserCommandService {

    UserAccount handle(RegisterUserCommand command);
}
