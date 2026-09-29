package com.osoterra.ososense.subscriptionbilling.domain.repositories;

import com.osoterra.ososense.subscriptionbilling.domain.model.PaymentTransaction;

public interface PaymentTransactionRepository {

    PaymentTransaction save(PaymentTransaction transaction);

    boolean existsByExternalReference(String externalReference);
}
