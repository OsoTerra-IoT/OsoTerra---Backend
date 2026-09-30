package com.osoterra.ososense.subscriptionbilling.application.internal.commandservices;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlanId;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.services.RequestSubscriptionCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.RequestSubscriptionCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class RequestSubscriptionCommandServiceImpl implements RequestSubscriptionCommandService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    RequestSubscriptionCommandServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionRepository subscriptionRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription handle(RequestSubscriptionCommand command) {
        SubscriptionPlan plan = subscriptionPlanRepository
                .findById(new SubscriptionPlanId(command.subscriptionPlanId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscription plan not found for id " + command.subscriptionPlanId()));
        Subscription subscription = Subscription.request(command.userAccountId(), plan);
        return subscriptionRepository.save(subscription);
    }
}
