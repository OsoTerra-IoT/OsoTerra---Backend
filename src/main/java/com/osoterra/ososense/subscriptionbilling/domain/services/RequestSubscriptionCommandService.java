package com.osoterra.ososense.subscriptionbilling.domain.services;

import com.osoterra.ososense.subscriptionbilling.domain.model.Subscription;

public interface RequestSubscriptionCommandService {

    Subscription handle(RequestSubscriptionCommand command);
}
