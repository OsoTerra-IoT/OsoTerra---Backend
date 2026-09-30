package com.osoterra.ososense.subscriptionbilling.domain.services;

import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransaction;

public interface RecordPaymentCommandService {

    PaymentTransaction handle(RecordPaymentCommand command);
}
