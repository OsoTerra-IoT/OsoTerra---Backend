package com.osoterra.ososense.identityaccess.domain.services;

import com.osoterra.ososense.identityaccess.domain.model.UserAccount;

import java.time.Instant;

public record AuthenticationResult(UserAccount account, String token, Instant expiresAt) {
}
