package com.osoterra.ososense.subscriptionbilling.domain.services;

import java.math.BigDecimal;

public record RecordPaymentCommand(
        Long subscriptionId, BigDecimal amount, String currency, String externalReference, boolean successful) {
}
