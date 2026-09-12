package com.osoterra.ososense.iam.domain.services;

import com.osoterra.ososense.iam.domain.model.PasswordHash;

/**
 * Abstracts the password hashing policy so the domain never depends on a concrete algorithm.
 */
public interface PasswordHashingService {

    PasswordHash hash(String rawPassword);

    boolean matches(String rawPassword, PasswordHash hash);
}
