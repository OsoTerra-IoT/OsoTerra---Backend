package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.repositories.CorrectiveActionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaCorrectiveActionRepository implements CorrectiveActionRepository {

    private final SpringDataCorrectiveActionJpaRepository springDataRepository;

    JpaCorrectiveActionRepository(SpringDataCorrectiveActionJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public CorrectiveAction save(CorrectiveAction action) {
        CorrectiveActionJpaEntity saved = springDataRepository.save(CorrectiveActionMapper.toEntity(action));
        return CorrectiveActionMapper.toDomain(saved);
    }

    @Override
    public Optional<CorrectiveAction> findBySalinityAlertId(Long salinityAlertId) {
        return springDataRepository.findBySalinityAlertId(salinityAlertId).map(CorrectiveActionMapper::toDomain);
    }
}
