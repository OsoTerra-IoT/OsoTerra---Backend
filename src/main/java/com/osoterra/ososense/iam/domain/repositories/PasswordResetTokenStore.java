package com.osoterra.ososense.iam.domain.repositories;

import com.osoterra.ososense.iam.domain.model.UserAccountId;

import java.time.Duration;
import java.util.Optional;

/**
 * Port for issuing and consuming single-use password reset tokens. The store owns the
 * token's persistence and expiry policy; it is not part of the {@code UserAccount} aggregate.
 */
public interface PasswordResetTokenStore {

    String issueTokenFor(UserAccountId userId, Duration validity);

    /**
     * Validates and marks the token as used, returning the account it was issued for.
     * Returns empty when the token does not exist, is already used, or has expired.
     */
    Optional<UserAccountId> consumeToken(String token);
}
