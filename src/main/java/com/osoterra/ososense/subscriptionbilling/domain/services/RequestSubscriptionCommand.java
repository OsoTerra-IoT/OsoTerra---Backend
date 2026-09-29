package com.osoterra.ososense.subscriptionbilling.domain.services;

public record RequestSubscriptionCommand(Long userAccountId, Long subscriptionPlanId) {
}
