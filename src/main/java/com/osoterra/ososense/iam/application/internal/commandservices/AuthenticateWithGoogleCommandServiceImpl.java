package com.osoterra.ososense.iam.application.internal.commandservices;

import com.osoterra.ososense.iam.domain.exceptions.InvalidCredentialsException;
import com.osoterra.ososense.iam.domain.gateways.GoogleIdentity;
import com.osoterra.ososense.iam.domain.gateways.GoogleTokenVerifier;
import com.osoterra.ososense.iam.domain.gateways.IssuedToken;
import com.osoterra.ososense.iam.domain.gateways.TokenIssuer;
import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.PersonName;
import com.osoterra.ososense.iam.domain.model.ProfessionalLicense;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import com.osoterra.ososense.iam.domain.services.AuthenticateWithGoogleCommand;
import com.osoterra.ososense.iam.domain.services.AuthenticateWithGoogleCommandService;
import com.osoterra.ososense.iam.domain.services.AuthenticationResult;
import org.springframework.stereotype.Service;

@Service
class AuthenticateWithGoogleCommandServiceImpl implements AuthenticateWithGoogleCommandService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserAccountRepository userAccountRepository;
    private final TokenIssuer tokenIssuer;

    AuthenticateWithGoogleCommandServiceImpl(
            GoogleTokenVerifier googleTokenVerifier,
            UserAccountRepository userAccountRepository,
            TokenIssuer tokenIssuer) {
        this.googleTokenVerifier = googleTokenVerifier;
        this.userAccountRepository = userAccountRepository;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthenticationResult handle(AuthenticateWithGoogleCommand command) {
        GoogleIdentity identity = googleTokenVerifier.verify(command.idToken()).orElseThrow(InvalidCredentialsException::new);
        EmailAddress email = new EmailAddress(identity.email());

        UserAccount account = userAccountRepository
                .findByEmail(email)
                .map(existing -> linkIfNeeded(existing, identity))
                .orElseGet(() -> registerFromGoogle(command, identity, email));

        IssuedToken token = tokenIssuer.issueFor(account);
        return new AuthenticationResult(account, token.value(), token.expiresAt());
    }

    private UserAccount linkIfNeeded(UserAccount existing, GoogleIdentity identity) {
        if (existing.getGoogleAccountId().isEmpty()) {
            existing.linkGoogleAccount(identity.subjectId());
            return userAccountRepository.save(existing);
        }
        return existing;
    }

    private UserAccount registerFromGoogle(AuthenticateWithGoogleCommand command, GoogleIdentity identity, EmailAddress email) {
        if (identity.firstName() == null || identity.lastName() == null) {
            throw new IllegalArgumentException("The Google account did not provide a first and last name");
        }
        ProfessionalLicense license = command.professionalLicenseNumber() == null
                ? null
                : new ProfessionalLicense(command.professionalLicenseNumber());
        UserAccount created = UserAccount.registerViaGoogle(
                email,
                new PersonName(identity.firstName(), identity.lastName()),
                command.role(),
                license,
                identity.subjectId());
        return userAccountRepository.save(created);
    }
}
