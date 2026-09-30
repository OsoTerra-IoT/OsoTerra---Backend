package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.repositories.NotificationPreferenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaNotificationPreferenceRepository implements NotificationPreferenceRepository {

    private final SpringDataNotificationPreferenceJpaRepository springDataRepository;

    JpaNotificationPreferenceRepository(SpringDataNotificationPreferenceJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public NotificationPreference save(NotificationPreference preference) {
        NotificationPreferenceJpaEntity saved = springDataRepository.save(NotificationPreferenceMapper.toEntity(preference));
        return NotificationPreferenceMapper.toDomain(saved);
    }

    @Override
    public Optional<NotificationPreference> findByUserAccountId(Long userAccountId) {
        return springDataRepository.findByUserAccountId(userAccountId).map(NotificationPreferenceMapper::toDomain);
    }
}
