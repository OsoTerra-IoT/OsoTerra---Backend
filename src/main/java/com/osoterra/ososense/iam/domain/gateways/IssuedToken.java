package com.osoterra.ososense.iam.domain.gateways;

import java.time.Instant;

public record IssuedToken(String value, Instant expiresAt) {
}
