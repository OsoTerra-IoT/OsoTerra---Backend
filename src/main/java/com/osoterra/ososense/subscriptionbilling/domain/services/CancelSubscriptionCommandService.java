package com.osoterra.ososense.subscriptionbilling.domain.services;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;

public interface CancelSubscriptionCommandService {

    Subscription handle(CancelSubscriptionCommand command);
}
