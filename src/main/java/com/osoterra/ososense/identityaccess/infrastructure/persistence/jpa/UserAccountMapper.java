package com.osoterra.ososense.identityaccess.infrastructure.persistence.jpa;

import com.osoterra.ososense.identityaccess.domain.model.EmailAddress;
import com.osoterra.ososense.identityaccess.domain.model.PasswordHash;
import com.osoterra.ososense.identityaccess.domain.model.PersonName;
import com.osoterra.ososense.identityaccess.domain.model.ProfessionalLicense;
import com.osoterra.ososense.identityaccess.domain.model.UserAccount;
import com.osoterra.ososense.identityaccess.domain.model.UserAccountId;

final class UserAccountMapper {

    private UserAccountMapper() {
    }

    static UserAccount toDomain(UserAccountJpaEntity entity) {
        ProfessionalLicense license = entity.getProfessionalLicense() == null
                ? null
                : new ProfessionalLicense(entity.getProfessionalLicense());

        return UserAccount.reconstruct(
                new UserAccountId(entity.getId()),
                new EmailAddress(entity.getEmail()),
                new PasswordHash(entity.getPasswordHash(), entity.getHashAlgorithm()),
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
                account.getPasswordHash().value(),
                account.getPasswordHash().algorithm(),
                account.getName().firstName(),
                account.getName().lastName(),
                account.getRole(),
                account.getLicense().map(ProfessionalLicense::number).orElse(null),
                account.isActive(),
                account.getCreatedAt(),
                null);
    }
}
