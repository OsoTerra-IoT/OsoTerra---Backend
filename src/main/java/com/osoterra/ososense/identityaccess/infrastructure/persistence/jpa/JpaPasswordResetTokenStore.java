package com.osoterra.ososense.identityaccess.infrastructure.persistence.jpa;

import com.osoterra.ososense.identityaccess.domain.repositories.PasswordResetTokenStore;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
class JpaPasswordResetTokenStore implements PasswordResetTokenStore {

    private final SpringDataPasswordResetTokenJpaRepository repository;

    JpaPasswordResetTokenStore(SpringDataPasswordResetTokenJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public String issueTokenFor(UserAccountId userId, Duration validity) {
        String token = UUID.randomUUID().toString();
        PasswordResetTokenJpaEntity entity = new PasswordResetTokenJpaEntity(
                null, userId.value(), token, LocalDateTime.now().plus(validity), false);
        repository.save(entity);
        return token;
    }

    @Override
    public Optional<UserAccountId> consumeToken(String token) {
        Optional<PasswordResetTokenJpaEntity> found = repository.findByToken(token);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        PasswordResetTokenJpaEntity entity = found.get();
        if (entity.isUsed() || entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }

        entity.setUsed(true);
        repository.save(entity);
        return Optional.of(new UserAccountId(entity.getUserAccountId()));
    }
}
