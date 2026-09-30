package com.osoterra.ososense.subscriptionbilling.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root for a recorded payment gateway transaction. No card or payment method
 * data is ever stored here — only the external reference and outcome the gateway reports.
 */
public final class PaymentTransaction extends AggregateRoot<PaymentTransactionId> {

    private final SubscriptionId subscriptionId;
    private final BigDecimal amount;
    private final String currency;
    private final String externalReference;
    private final boolean isSuccessful;
    private final LocalDateTime processedAt;

    private PaymentTransaction(
            PaymentTransactionId id, SubscriptionId subscriptionId, BigDecimal amount, String currency,
            String externalReference, boolean isSuccessful, LocalDateTime processedAt) {
        super(id);
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.currency = currency;
        this.externalReference = externalReference;
        this.isSuccessful = isSuccessful;
        this.processedAt = processedAt;
    }

    public static PaymentTransaction record(
            SubscriptionId subscriptionId, BigDecimal amount, String currency, String externalReference,
            boolean isSuccessful) {
        Objects.requireNonNull(subscriptionId, "subscriptionId");
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(externalReference, "externalReference");
        return new PaymentTransaction(
                null, subscriptionId, amount, currency, externalReference, isSuccessful, LocalDateTime.now());
    }

    public static PaymentTransaction reconstruct(
            PaymentTransactionId id, SubscriptionId subscriptionId, BigDecimal amount, String currency,
            String externalReference, boolean isSuccessful, LocalDateTime processedAt) {
        return new PaymentTransaction(id, subscriptionId, amount, currency, externalReference, isSuccessful, processedAt);
    }

    public SubscriptionId getSubscriptionId() {
        return subscriptionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}
