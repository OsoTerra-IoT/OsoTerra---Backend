package com.osoterra.ososense.subscriptionbilling.application.internal.queryservices;

import com.osoterra.ososense.subscriptionbilling.domain.gateways.ActivePlotCounter;
import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.services.SubscriptionBillingQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
class SubscriptionBillingQueryServiceImpl implements SubscriptionBillingQueryService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ActivePlotCounter activePlotCounter;

    SubscriptionBillingQueryServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionRepository subscriptionRepository,
            ActivePlotCounter activePlotCounter) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.activePlotCounter = activePlotCounter;
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

    @Override
    public Optional<Integer> findPlotLimitByUserAccountId(Long userAccountId) {
        LocalDate today = LocalDate.now();
        return subscriptionRepository.findByUserAccountId(userAccountId).stream()
                .filter(subscription -> subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .filter(subscription -> subscription.getPeriodEndDate().map(end -> !end.isBefore(today)).orElse(true))
                .map(Subscription::getQuotaTotal)
                .max(Comparator.naturalOrder());
    }

    @Override
    public int findConsumedQuotaByUserAccountId(Long userAccountId) {
        return activePlotCounter.countActivePlotsOfOwner(userAccountId);
    }
}
