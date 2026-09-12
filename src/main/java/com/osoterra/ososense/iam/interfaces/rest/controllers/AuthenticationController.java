package com.osoterra.ososense.iam.interfaces.rest.controllers;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserRole;
import com.osoterra.ososense.iam.domain.services.AuthenticateUserCommand;
import com.osoterra.ososense.iam.domain.services.AuthenticateUserCommandService;
import com.osoterra.ososense.iam.domain.services.AuthenticationResult;
import com.osoterra.ososense.iam.domain.services.RegisterUserCommand;
import com.osoterra.ososense.iam.domain.services.RegisterUserCommandService;
import com.osoterra.ososense.iam.domain.services.RequestPasswordResetCommand;
import com.osoterra.ososense.iam.domain.services.RequestPasswordResetCommandService;
import com.osoterra.ososense.iam.domain.services.ResetPasswordCommand;
import com.osoterra.ososense.iam.domain.services.ResetPasswordCommandService;
import com.osoterra.ososense.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.RequestPasswordResetResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.ResetPasswordResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.SignInResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.SignUpResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController {

    private final RegisterUserCommandService registerUserCommandService;
    private final AuthenticateUserCommandService authenticateUserCommandService;
    private final RequestPasswordResetCommandService requestPasswordResetCommandService;
    private final ResetPasswordCommandService resetPasswordCommandService;
    private final UserAccountResourceAssembler userAccountResourceAssembler;

    AuthenticationController(
            RegisterUserCommandService registerUserCommandService,
            AuthenticateUserCommandService authenticateUserCommandService,
            RequestPasswordResetCommandService requestPasswordResetCommandService,
            ResetPasswordCommandService resetPasswordCommandService,
            UserAccountResourceAssembler userAccountResourceAssembler) {
        this.registerUserCommandService = registerUserCommandService;
        this.authenticateUserCommandService = authenticateUserCommandService;
        this.requestPasswordResetCommandService = requestPasswordResetCommandService;
        this.resetPasswordCommandService = resetPasswordCommandService;
        this.userAccountResourceAssembler = userAccountResourceAssembler;
    }

    @PostMapping("/signup")
    ResponseEntity<UserAccountResource> signUp(@Valid @RequestBody SignUpResource request) {
        UserAccount account = registerUserCommandService.handle(new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                UserRole.valueOf(request.role()),
                request.professionalLicenseNumber()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userAccountResourceAssembler.toResource(account));
    }

    @PostMapping("/signin")
    ResponseEntity<AuthenticatedUserResource> signIn(@Valid @RequestBody SignInResource request) {
        AuthenticationResult result =
                authenticateUserCommandService.handle(new AuthenticateUserCommand(request.email(), request.password()));

        return ResponseEntity.ok(new AuthenticatedUserResource(
                result.token(), result.expiresAt(), userAccountResourceAssembler.toResource(result.account())));
    }

    @PostMapping("/password-reset-requests")
    ResponseEntity<Void> requestPasswordReset(@Valid @RequestBody RequestPasswordResetResource request) {
        requestPasswordResetCommandService.handle(new RequestPasswordResetCommand(request.email()));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/password-resets")
    ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordResource request) {
        resetPasswordCommandService.handle(new ResetPasswordCommand(request.token(), request.newPassword()));
        return ResponseEntity.noContent().build();
    }
}
