package com.osoterra.ososense.iam.infrastructure.persistence.jpa;

import com.osoterra.ososense.iam.domain.model.EmailAddress;
import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.repositories.UserAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaUserAccountRepository implements UserAccountRepository {

    private final SpringDataUserAccountJpaRepository springDataRepository;

    JpaUserAccountRepository(SpringDataUserAccountJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public UserAccount save(UserAccount account) {
        UserAccountJpaEntity saved = springDataRepository.save(UserAccountMapper.toEntity(account));
        return UserAccountMapper.toDomain(saved);
    }

    @Override
    public Optional<UserAccount> findById(UserAccountId id) {
        return springDataRepository.findById(id.value()).map(UserAccountMapper::toDomain);
    }

    @Override
    public Optional<UserAccount> findByEmail(EmailAddress email) {
        return springDataRepository.findByEmail(email.value()).map(UserAccountMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return springDataRepository.existsByEmail(email.value());
    }
}
