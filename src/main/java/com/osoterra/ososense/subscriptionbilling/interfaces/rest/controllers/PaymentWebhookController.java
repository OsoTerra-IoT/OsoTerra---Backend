package com.osoterra.ososense.subscriptionbilling.interfaces.rest.controllers;

import com.osoterra.ososense.subscriptionbilling.domain.gateways.PaymentWebhookVerifier;
import com.osoterra.ososense.subscriptionbilling.domain.gateways.VerifiedPaymentEvent;
import com.osoterra.ososense.subscriptionbilling.domain.services.RecordPaymentCommand;
import com.osoterra.ososense.subscriptionbilling.domain.services.RecordPaymentCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * Open Host Service for the payment gateway's webhook. The raw request body is captured
 * as bytes (not parsed by a JSON converter first) because Stripe's signature is computed
 * over the exact bytes it sent.
 */
@RestController
@RequestMapping("/api/v1/payments")
class PaymentWebhookController {

    private final PaymentWebhookVerifier paymentWebhookVerifier;
    private final RecordPaymentCommandService recordPaymentCommandService;

    PaymentWebhookController(
            PaymentWebhookVerifier paymentWebhookVerifier, RecordPaymentCommandService recordPaymentCommandService) {
        this.paymentWebhookVerifier = paymentWebhookVerifier;
        this.recordPaymentCommandService = recordPaymentCommandService;
    }

    @PostMapping("/webhook")
    ResponseEntity<Void> receive(@RequestBody byte[] rawBody, @RequestHeader("Stripe-Signature") String signature) {
        String payload = new String(rawBody, StandardCharsets.UTF_8);
        paymentWebhookVerifier.verify(payload, signature).ifPresent(this::recordPayment);
        return ResponseEntity.ok().build();
    }

    private void recordPayment(VerifiedPaymentEvent event) {
        recordPaymentCommandService.handle(new RecordPaymentCommand(
                event.subscriptionId(), event.amount(), event.currency(), event.externalReference(),
                event.successful()));
    }
}
