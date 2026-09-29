package com.osoterra.ososense.subscriptionbilling.domain.gateways;

import java.util.Optional;

/**
 * Verifies and parses an inbound payment gateway webhook call. Implementations must
 * check the request signature before trusting any of its content.
 */
public interface PaymentWebhookVerifier {

    Optional<VerifiedPaymentEvent> verify(String payload, String signatureHeader);
}
