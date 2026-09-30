package com.osoterra.ososense.subscriptionbilling.domain.repositories;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findById(SubscriptionId id);

    List<Subscription> findByUserAccountId(Long userAccountId);

    List<Subscription> findByStatusAndPeriodEndDateBefore(SubscriptionStatus status, LocalDate date);
}
