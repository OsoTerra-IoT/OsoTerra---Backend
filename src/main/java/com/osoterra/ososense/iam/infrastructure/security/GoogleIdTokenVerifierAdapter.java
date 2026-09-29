package com.osoterra.ososense.iam.infrastructure.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.osoterra.ososense.iam.domain.gateways.GoogleIdentity;
import com.osoterra.ososense.iam.domain.gateways.GoogleTokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

/**
 * Verifies a Google-issued ID token against the platform's OAuth client id, using
 * Google's own signature and audience checks. Never trusts a token's claims without
 * this verification step.
 */
@Component
public class GoogleIdTokenVerifierAdapter implements GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleIdTokenVerifierAdapter(@Value("${app.security.google.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public Optional<GoogleIdentity> verify(String idToken) {
        try {
            GoogleIdToken verified = verifier.verify(idToken);
            if (verified == null) {
                return Optional.empty();
            }
            GoogleIdToken.Payload payload = verified.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            return Optional.of(new GoogleIdentity(payload.getEmail(), payload.getSubject(), firstName, lastName));
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
