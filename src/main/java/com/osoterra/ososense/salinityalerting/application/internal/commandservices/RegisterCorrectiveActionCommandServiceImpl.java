package com.osoterra.ososense.salinityalerting.application.internal.commandservices;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveActionType;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.repositories.CorrectiveActionRepository;
import com.osoterra.ososense.salinityalerting.domain.repositories.SalinityAlertRepository;
import com.osoterra.ososense.salinityalerting.domain.services.RegisterCorrectiveActionCommand;
import com.osoterra.ososense.salinityalerting.domain.services.RegisterCorrectiveActionCommandService;
import com.osoterra.ososense.shared.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Registering a corrective action means the producer addressed the alert, so the alert
 * is resolved as part of the same command.
 */
@Service
class RegisterCorrectiveActionCommandServiceImpl implements RegisterCorrectiveActionCommandService {

    private final SalinityAlertRepository salinityAlertRepository;
    private final CorrectiveActionRepository correctiveActionRepository;

    RegisterCorrectiveActionCommandServiceImpl(
            SalinityAlertRepository salinityAlertRepository, CorrectiveActionRepository correctiveActionRepository) {
        this.salinityAlertRepository = salinityAlertRepository;
        this.correctiveActionRepository = correctiveActionRepository;
    }

    @Override
    public CorrectiveAction handle(RegisterCorrectiveActionCommand command) {
        SalinityAlert alert = salinityAlertRepository
                .findById(new SalinityAlertId(command.salinityAlertId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Salinity alert not found for id " + command.salinityAlertId()));

        CorrectiveAction action = CorrectiveAction.register(
                command.salinityAlertId(), CorrectiveActionType.valueOf(command.actionType()), command.executedAt(),
                command.notes(), command.registeredBy());
        CorrectiveAction saved = correctiveActionRepository.save(action);

        alert.resolve();
        salinityAlertRepository.save(alert);

        return saved;
    }
}
