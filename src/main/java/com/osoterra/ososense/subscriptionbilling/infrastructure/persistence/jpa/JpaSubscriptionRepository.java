package com.osoterra.ososense.subscriptionbilling.infrastructure.persistence.jpa;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
class JpaSubscriptionRepository implements SubscriptionRepository {

    private final SpringDataSubscriptionJpaRepository springDataRepository;

    JpaSubscriptionRepository(SpringDataSubscriptionJpaRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionJpaEntity saved = springDataRepository.save(SubscriptionMapper.toEntity(subscription));
        return SubscriptionMapper.toDomain(saved);
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        return springDataRepository.findById(id.value()).map(SubscriptionMapper::toDomain);
    }

    @Override
    public List<Subscription> findByUserAccountId(Long userAccountId) {
        return springDataRepository.findByUserAccountId(userAccountId).stream().map(SubscriptionMapper::toDomain).toList();
    }

    @Override
    public List<Subscription> findByStatusAndPeriodEndDateBefore(SubscriptionStatus status, LocalDate date) {
        return springDataRepository.findByStatusAndPeriodEndDateBefore(status, date).stream()
                .map(SubscriptionMapper::toDomain)
                .toList();
    }
}
