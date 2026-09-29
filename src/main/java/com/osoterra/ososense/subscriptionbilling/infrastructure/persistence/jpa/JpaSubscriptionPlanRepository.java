package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class JpaSubscriptionPlanRepository implements SubscriptionPlanRepository {

    private final SpringDataSubscriptionPlanJpaRepository springDataRepository;

    JpaSubscriptionPlanRepository(SpringDataSubscriptionPlanJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan plan) {
        SubscriptionPlanJpaEntity saved = springDataRepository.save(SubscriptionPlanMapper.toEntity(plan));
        return SubscriptionPlanMapper.toDomain(saved);
    }

    @Override
    public Optional<SubscriptionPlan> findById(SubscriptionPlanId id) {
        return springDataRepository.findById(id.value()).map(SubscriptionPlanMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return springDataRepository.existsByName(name);
    }

    @Override
    public List<SubscriptionPlan> findByIsActiveTrue() {
        return springDataRepository.findByActiveTrue().stream().map(SubscriptionPlanMapper::toDomain).toList();
    }
}
