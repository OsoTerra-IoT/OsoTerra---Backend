package com.osoterra.ososense.iam.infrastructure.persistence.jpa;

import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.PasswordHash;
import com.osoterra.ososense.iam.domain.model.PersonName;
import com.osoterra.ososense.iam.domain.model.ProfessionalLicense;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;

final class UserAccountMapper {

    private UserAccountMapper() {
    }

    static UserAccount toDomain(UserAccountJpaEntity entity) {
        ProfessionalLicense license = entity.getProfessionalLicense() == null
                ? null
                : new ProfessionalLicense(entity.getProfessionalLicense());
        PasswordHash passwordHash = entity.getPasswordHash() == null
                ? null
                : new PasswordHash(entity.getPasswordHash(), entity.getHashAlgorithm());

        return UserAccount.reconstruct(
                new UserAccountId(entity.getId()),
                new EmailAddress(entity.getEmail()),
                passwordHash,
                entity.getGoogleAccountId(),
                new PersonName(entity.getFirstName(), entity.getLastName()),
                entity.getRole(),
                license,
                entity.isActive(),
                entity.getCreatedAt());
    }

    static UserAccountJpaEntity toEntity(UserAccount account) {
        Long id = account.getId() == null ? null : account.getId().value();
        return new UserAccountJpaEntity(
                id,
                account.getEmail().value(),
                account.getPasswordHash().map(PasswordHash::value).orElse(null),
                account.getPasswordHash().map(PasswordHash::algorithm).orElse(null),
                account.getGoogleAccountId().orElse(null),
                account.getName().firstName(),
                account.getName().lastName(),
                account.getRole(),
                account.getLicense().map(ProfessionalLicense::number).orElse(null),
                account.isActive(),
                account.getCreatedAt(),
                null);
    }
}
