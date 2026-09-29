package com.osoterra.ososense.iam.domain.gateways;

import java.util.Optional;

public interface GoogleTokenVerifier {

    Optional<GoogleIdentity> verify(String idToken);
}
