package com.osoterra.ososense.identityaccess.interfaces.rest.resources;

import java.time.Instant;

public record AuthenticatedUserResource(String token, Instant expiresAt, UserAccountResource user) {
}
