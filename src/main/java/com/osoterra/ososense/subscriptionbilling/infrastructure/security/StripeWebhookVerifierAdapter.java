package com.osoterra.ososense.subscriptionbilling.infrastructure.security;

import com.osoterra.ososense.subscriptionbilling.domain.gateways.PaymentWebhookVerifier;
import com.osoterra.ososense.subscriptionbilling.domain.gateways.VerifiedPaymentEvent;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Verifies the {@code Stripe-Signature} header before trusting any webhook payload, per
 * Stripe's own recommended integration pattern. Only {@code payment_intent.succeeded}
 * and {@code payment_intent.payment_failed} are handled; every other event type is
 * ignored. Expects the subscription id to travel in the payment intent's metadata under
 * the key {@code subscription_id}, set when the checkout was created.
 */
@Component
public class StripeWebhookVerifierAdapter implements PaymentWebhookVerifier {

    private final String webhookSecret;

    public StripeWebhookVerifierAdapter(@Value("${app.security.stripe.webhook-secret}") String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    @Override
    public Optional<VerifiedPaymentEvent> verify(String payload, String signatureHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return Optional.empty();
        }

        boolean succeeded = "payment_intent.succeeded".equals(event.getType());
        boolean failed = "payment_intent.payment_failed".equals(event.getType());
        if (!succeeded && !failed) {
            return Optional.empty();
        }

        return event.getDataObjectDeserializer().getObject()
                .filter(PaymentIntent.class::isInstance)
                .map(PaymentIntent.class::cast)
                .flatMap(paymentIntent -> toVerifiedEvent(paymentIntent, succeeded));
    }

    private Optional<VerifiedPaymentEvent> toVerifiedEvent(PaymentIntent paymentIntent, boolean succeeded) {
        String subscriptionIdValue = paymentIntent.getMetadata().get("subscription_id");
        if (subscriptionIdValue == null) {
            return Optional.empty();
        }
        BigDecimal amount = BigDecimal.valueOf(paymentIntent.getAmount()).movePointLeft(2);
        return Optional.of(new VerifiedPaymentEvent(
                Long.valueOf(subscriptionIdValue),
                amount,
                paymentIntent.getCurrency().toUpperCase(),
                paymentIntent.getId(),
                succeeded));
    }
}
