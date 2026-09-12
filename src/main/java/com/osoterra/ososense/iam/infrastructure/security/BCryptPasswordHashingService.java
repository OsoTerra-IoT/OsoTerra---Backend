package com.osoterra.ososense.iam.infrastructure.security;

import com.osoterra.ososense.iam.domain.model.PasswordHash;
import com.osoterra.ososense.iam.domain.services.PasswordHashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordHashingService implements PasswordHashingService {

    private static final String ALGORITHM = "BCRYPT";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public PasswordHash hash(String rawPassword) {
        return new PasswordHash(encoder.encode(rawPassword), ALGORITHM);
    }

    @Override
    public boolean matches(String rawPassword, PasswordHash hash) {
        return encoder.matches(rawPassword, hash.value());
    }
}
