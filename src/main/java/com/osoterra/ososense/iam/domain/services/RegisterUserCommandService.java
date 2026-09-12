package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.UserAccount;

public interface RegisterUserCommandService {

    UserAccount handle(RegisterUserCommand command);
}
