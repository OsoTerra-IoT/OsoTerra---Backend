package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.UserAccount;

import java.time.Instant;

public record AuthenticationResult(UserAccount account, String token, Instant expiresAt) {
}
