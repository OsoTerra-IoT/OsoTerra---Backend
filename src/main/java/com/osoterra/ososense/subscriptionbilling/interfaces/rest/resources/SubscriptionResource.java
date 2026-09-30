package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionResource(
        Long id, Long userAccountId, Long subscriptionPlanId, String status, int quotaTotal, int quotaConsumed,
        LocalDate periodStartDate, LocalDate periodEndDate, LocalDateTime createdAt) {
}
