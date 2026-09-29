package com.osoterra.ososense.subscriptionbilling.application.internal.queryservices;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.services.SubscriptionBillingQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class SubscriptionBillingQueryServiceImpl implements SubscriptionBillingQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    SubscriptionBillingQueryServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionRepository subscriptionRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<SubscriptionPlan> findActivePlans() {
        return subscriptionPlanRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<SubscriptionPlan> findPlanById(SubscriptionPlanId id) {
        return subscriptionPlanRepository.findById(id);
    }

    @Override
    public Optional<Subscription> findSubscriptionById(SubscriptionId id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public List<Subscription> findSubscriptionsByUserAccountId(Long userAccountId) {
        return subscriptionRepository.findByUserAccountId(userAccountId);
    }
}
