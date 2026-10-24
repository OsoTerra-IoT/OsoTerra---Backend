package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.gateways.PasswordResetNotifier;
import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.repositories.PasswordResetTokenStore;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.iam.domain.services.RequestPasswordResetCommand;
import com.osoterra.ososense.iam.domain.services.RequestPasswordResetCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
class RequestPasswordResetCommandServiceImpl implements RequestPasswordResetCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestPasswordResetCommandServiceImpl.class);
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
                    try {
                        passwordResetNotifier.notifyResetRequested(account.getEmail().value(), token);
                    } catch (RuntimeException ex) {
                        // Answer the same as for an unknown email: a mail failure must not reveal the account.
                        LOGGER.warn("Could not send the password reset email to account {}: {}",
                                account.getId(), ex.getMessage());
                    }
                });
    }
}
