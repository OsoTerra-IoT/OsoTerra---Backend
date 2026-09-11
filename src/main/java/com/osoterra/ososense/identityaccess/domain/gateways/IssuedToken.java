package com.osoterra.ososense.identityaccess.domain.gateways;

import java.time.Instant;

public record IssuedToken(String value, Instant expiresAt) {
}
