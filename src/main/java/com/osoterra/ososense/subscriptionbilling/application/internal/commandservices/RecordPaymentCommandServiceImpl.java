package com.osoterra.ososense.subscriptionbilling.application.internal.commandservices;

import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransaction;
import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionId;
import com.osoterra.ososense.subscriptionbilling.domain.model.SubscriptionPlan;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.PaymentTransactionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionPlanRepository;
import com.osoterra.ososense.subscriptionbilling.domain.repositories.SubscriptionRepository;
import com.osoterra.ososense.subscriptionbilling.domain.services.RecordPaymentCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.RecordPaymentCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class RecordPaymentCommandServiceImpl implements RecordPaymentCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    RecordPaymentCommandServiceImpl(
            SubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            PaymentTransactionRepository paymentTransactionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @Override
    public PaymentTransaction handle(RecordPaymentCommand command) {
        Subscription subscription = subscriptionRepository
                .findById(new SubscriptionId(command.subscriptionId()))
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found for id " + command.subscriptionId()));

        PaymentTransaction transaction = PaymentTransaction.record(
                subscription.getId(), command.amount(), command.currency(), command.externalReference(),
                command.successful());
        PaymentTransaction saved = paymentTransactionRepository.save(transaction);

        if (command.successful()) {
            SubscriptionPlan plan = subscriptionPlanRepository
                    .findById(subscription.getSubscriptionPlanId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Subscription plan not found for id " + subscription.getSubscriptionPlanId().value()));
            subscription.activate(plan.getBillingCycle());
            subscriptionRepository.save(subscription);
        }

        return saved;
    }
}
