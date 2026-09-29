package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import org.springframework.stereotype.Component;

@Component
public class CorrectiveActionResourceAssembler {

    public CorrectiveActionResource toResource(CorrectiveAction action) {
        return new CorrectiveActionResource(
                action.getId().value(),
                action.getSalinityAlertId(),
                action.getActionType().name(),
                action.getExecutedAt(),
                action.getNotes().orElse(null),
                action.getRegisteredBy(),
                action.getRegisteredAt());
    }
}
