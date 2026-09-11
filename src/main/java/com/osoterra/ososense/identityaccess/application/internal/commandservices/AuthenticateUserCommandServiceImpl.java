package com.osoterra.ososense.identityaccess.application.internal.commandservices;

import com.osoterra.ososense.identityaccess.domain.exceptions.InvalidCredentialsException;
import com.osoterra.ososense.identityaccess.domain.gateways.IssuedToken;
import com.osoterra.ososense.identityaccess.domain.gateways.TokenIssuer;
import com.osoterra.ososense.identityaccess.domain.model.EmailAddress;
import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.identityaccess.domain.services.AuthenticateUserCommand;
import com.osoterra.ososense.identityaccess.domain.services.AuthenticateUserCommandService;
import com.osoterra.ososense.identityaccess.domain.services.AuthenticationResult;
import com.osoterra.ososense.identityaccess.domain.services.PasswordHashingService;
import org.springframework.stereotype.Service;

@Service
class AuthenticateUserCommandServiceImpl implements AuthenticateUserCommandService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordHashingService passwordHashingService;
    private final TokenIssuer tokenIssuer;

    AuthenticateUserCommandServiceImpl(
            UserAccountRepository userAccountRepository,
            PasswordHashingService passwordHashingService,
            TokenIssuer tokenIssuer) {
        this.userAccountRepository = userAccountRepository;
        this.passwordHashingService = passwordHashingService;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthenticationResult handle(AuthenticateUserCommand command) {
        UserAccount account = userAccountRepository
                .findByEmail(new EmailAddress(command.email()))
                .filter(UserAccount::isActive)
                .orElseThrow(InvalidCredentialsException::new);

        if (!account.verifyPassword(command.rawPassword(), passwordHashingService)) {
            throw new InvalidCredentialsException();
        }

        IssuedToken token = tokenIssuer.issueFor(account);
        return new AuthenticationResult(account, token.value(), token.expiresAt());
    }
}
