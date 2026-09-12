package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.PersonName;
import com.osoterra.ososense.iam.domain.model.ProfessionalLicense;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.iam.domain.services.PasswordHashingService;
import com.osoterra.ososense.iam.domain.services.RegisterUserCommand;
import com.osoterra.ososense.iam.domain.services.RegisterUserCommandService;
import com.osoterra.ososense.shared.BusinessRuleViolationException;
import org.springframework.stereotype.Service;

@Service
class RegisterUserCommandServiceImpl implements RegisterUserCommandService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordHashingService passwordHashingService;

    RegisterUserCommandServiceImpl(
            UserAccountRepository userAccountRepository, PasswordHashingService passwordHashingService) {
        this.userAccountRepository = userAccountRepository;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public UserAccount handle(RegisterUserCommand command) {
        EmailAddress email = new EmailAddress(command.email());
        if (userAccountRepository.existsByEmail(email)) {
            throw new BusinessRuleViolationException("An account already exists for this email");
        }

        ProfessionalLicense license = command.professionalLicenseNumber() == null
                ? null
                : new ProfessionalLicense(command.professionalLicenseNumber());

        UserAccount account = UserAccount.register(
                email,
                passwordHashingService.hash(command.rawPassword()),
                new PersonName(command.firstName(), command.lastName()),
                command.role(),
                license);

        return userAccountRepository.save(account);
    }
}
