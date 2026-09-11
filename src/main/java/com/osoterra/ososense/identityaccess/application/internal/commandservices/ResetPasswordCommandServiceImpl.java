package com.osoterra.ososense.identityaccess.application.internal.commandservices;

import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import com.osoterra.ososense.identityaccess.domain.repositories.PasswordResetTokenStore;
import com.osoterra.ososense.identityaccess.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.identityaccess.domain.services.PasswordHashingService;
import com.osoterra.ososense.identityaccess.domain.services.ResetPasswordCommand;
import com.osoterra.ososense.identityaccess.domain.services.ResetPasswordCommandService;
import com.osoterra.ososense.shared.BusinessRuleViolationException;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class ResetPasswordCommandServiceImpl implements ResetPasswordCommandService {

    private final PasswordResetTokenStore passwordResetTokenStore;
    private final UserAccountRepository userAccountRepository;
    private final PasswordHashingService passwordHashingService;

    ResetPasswordCommandServiceImpl(
            PasswordResetTokenStore passwordResetTokenStore,
            UserAccountRepository userAccountRepository,
            PasswordHashingService passwordHashingService) {
        this.passwordResetTokenStore = passwordResetTokenStore;
        this.userAccountRepository = userAccountRepository;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public void handle(ResetPasswordCommand command) {
        UserAccountId userId = passwordResetTokenStore
                .consumeToken(command.token())
                .orElseThrow(() -> new BusinessRuleViolationException("The reset token is invalid or has expired"));

        UserAccount account = userAccountRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for id " + userId));

        account.changePassword(passwordHashingService.hash(command.newRawPassword()));
        userAccountRepository.save(account);
    }
}
