package com.osoterra.ososense.subscriptionbilling.application.internal.commandservices;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.services.CancelSubscriptionCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.CancelSubscriptionCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class CancelSubscriptionCommandServiceImpl implements CancelSubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;

    CancelSubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription handle(CancelSubscriptionCommand command) {
        Subscription subscription = subscriptionRepository
                .findById(new SubscriptionId(command.subscriptionId()))
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id " + command.subscriptionId()));
        subscription.cancel();
        return subscriptionRepository.save(subscription);
    }
}
