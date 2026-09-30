package com.osoterra.ososense.subscriptionbilling.infrastructure;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionStatus;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Suspends active subscriptions once their billing period has ended without a renewal.
 */
@Component
public class SubscriptionExpirationScheduler {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionExpirationScheduler(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void suspendExpiredSubscriptions() {
        for (Subscription subscription :
                subscriptionRepository.findByStatusAndPeriodEndDateBefore(SubscriptionStatus.ACTIVE, LocalDate.now())) {
            subscription.suspend();
            subscriptionRepository.save(subscription);
        }
    }
}
