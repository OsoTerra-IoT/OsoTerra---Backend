package com.osoterra.ososense.identityaccess.application.internal.commandservices;

import com.osoterra.ososense.identityaccess.domain.gateways.PasswordResetNotifier;
import com.osoterra.ososense.identityaccess.domain.model.EmailAddress;
import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.repositories.PasswordResetTokenStore;
import com.osoterra.ososense.identityaccess.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.identityaccess.domain.services.RequestPasswordResetCommand;
import com.osoterra.ososense.identityaccess.domain.services.RequestPasswordResetCommandService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
class RequestPasswordResetCommandServiceImpl implements RequestPasswordResetCommandService {

    private static final Duration TOKEN_VALIDITY = Duration.ofHours(1);

    private final UserAccountRepository userAccountRepository;
    private final PasswordResetTokenStore passwordResetTokenStore;
    private final PasswordResetNotifier passwordResetNotifier;

    RequestPasswordResetCommandServiceImpl(
            UserAccountRepository userAccountRepository,
            PasswordResetTokenStore passwordResetTokenStore,
            PasswordResetNotifier passwordResetNotifier) {
        this.userAccountRepository = userAccountRepository;
        this.passwordResetTokenStore = passwordResetTokenStore;
        this.passwordResetNotifier = passwordResetNotifier;
    }

    @Override
    public void handle(RequestPasswordResetCommand command) {
        userAccountRepository
                .findByEmail(new EmailAddress(command.email()))
                .filter(UserAccount::isActive)
                .ifPresent(account -> {
                    String token = passwordResetTokenStore.issueTokenFor(account.getId(), TOKEN_VALIDITY);
                    passwordResetNotifier.notifyResetRequested(account.getEmail().value(), token);
                });
    }
}
