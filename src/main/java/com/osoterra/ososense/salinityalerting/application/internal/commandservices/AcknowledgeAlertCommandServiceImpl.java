package com.osoterra.ososense.salinityalerting.application.internal.commandservices;

import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlertId;
import com.osoterra.ososense.salinityalerting.domain.repositories.SalinityAlertRepository;
import com.osoterra.ososense.salinityalerting.domain.services.AcknowledgeAlertCommand;
import com.osoterra.ososense.salinityalerting.domain.services.AcknowledgeAlertCommandService;
import com.osoterra.ososense.shared.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AcknowledgeAlertCommandServiceImpl implements AcknowledgeAlertCommandService {

    private final SalinityAlertRepository salinityAlertRepository;

    AcknowledgeAlertCommandServiceImpl(SalinityAlertRepository salinityAlertRepository) {
        this.salinityAlertRepository = salinityAlertRepository;
    }

    @Override
    public SalinityAlert handle(AcknowledgeAlertCommand command) {
        SalinityAlert alert = salinityAlertRepository
                .findById(new SalinityAlertId(command.alertId()))
                .orElseThrow(() -> new EntityNotFoundException("Salinity alert not found for id " + command.alertId()));
        alert.acknowledge(command.byUserId());
        return salinityAlertRepository.save(alert);
    }
}
