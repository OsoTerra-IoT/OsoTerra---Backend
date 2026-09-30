package com.osoterra.ososense.subscriptionbilling.domain.gateways;

import java.math.BigDecimal;

public record VerifiedPaymentEvent(
        Long subscriptionId, BigDecimal amount, String currency, String externalReference, boolean successful) {
}
