package com.osoterra.ososense.salinityalerting.domain.services;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;

public interface RegisterCorrectiveActionCommandService {

    CorrectiveAction handle(RegisterCorrectiveActionCommand command);
}
