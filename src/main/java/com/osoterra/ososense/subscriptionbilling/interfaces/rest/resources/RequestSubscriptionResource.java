package com.osoterra.ososense.subscriptionbilling.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record RequestSubscriptionResource(@NotNull Long subscriptionPlanId) {
}
