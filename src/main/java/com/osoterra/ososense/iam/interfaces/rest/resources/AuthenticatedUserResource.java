package com.osoterra.ososense.iam.interfaces.rest.resources;

import java.time.Instant;

public record AuthenticatedUserResource(String token, Instant expiresAt, UserAccountResource user) {
}
