package com.osoterra.ososense.salinityalerting.domain.services;

import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;

public interface AcknowledgeAlertCommandService {

    SalinityAlert handle(AcknowledgeAlertCommand command);
}
