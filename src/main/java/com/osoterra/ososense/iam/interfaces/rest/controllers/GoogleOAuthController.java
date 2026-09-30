package com.osoterra.ososense.iam.interfaces.rest.controllers;

import com.osoterra.ososense.iam.domain.model.UserRole;
import com.osoterra.ososense.iam.domain.services.AuthenticateWithGoogleCommand;
import com.osoterra.ososense.iam.domain.services.AuthenticateWithGoogleCommandService;
import com.osoterra.ososense.iam.domain.services.AuthenticationResult;
import com.osoterra.ososense.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.GoogleSignInResource;
import com.osoterra.ososense.iam.interfaces.rest.resources.UserAccountResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class GoogleOAuthController {

    private final AuthenticateWithGoogleCommandService authenticateWithGoogleCommandService;
    private final UserAccountResourceAssembler userAccountResourceAssembler;

    GoogleOAuthController(
            AuthenticateWithGoogleCommandService authenticateWithGoogleCommandService,
            UserAccountResourceAssembler userAccountResourceAssembler) {
        this.authenticateWithGoogleCommandService = authenticateWithGoogleCommandService;
        this.userAccountResourceAssembler = userAccountResourceAssembler;
    }

    @PostMapping("/google")
    ResponseEntity<AuthenticatedUserResource> signInWithGoogle(@Valid @RequestBody GoogleSignInResource request) {
        AuthenticationResult result = authenticateWithGoogleCommandService.handle(new AuthenticateWithGoogleCommand(
                request.idToken(), UserRole.valueOf(request.role()), request.professionalLicenseNumber()));

        return ResponseEntity.ok(new AuthenticatedUserResource(
                result.token(), result.expiresAt(), userAccountResourceAssembler.toResource(result.account())));
    }
}
