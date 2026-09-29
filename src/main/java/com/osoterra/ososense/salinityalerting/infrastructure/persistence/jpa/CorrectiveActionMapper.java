package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveAction;
import com.osoterra.ososense.salinityalerting.domain.model.CorrectiveActionId;

final class CorrectiveActionMapper {

    private CorrectiveActionMapper() {
    }

    static CorrectiveAction toDomain(CorrectiveActionJpaEntity entity) {
        return CorrectiveAction.reconstruct(
                new CorrectiveActionId(entity.getId()),
                entity.getSalinityAlertId(),
                entity.getActionType(),
                entity.getExecutedAt(),
                entity.getNotes(),
                entity.getRegisteredBy(),
                entity.getRegisteredAt());
    }

    static CorrectiveActionJpaEntity toEntity(CorrectiveAction action) {
        Long id = action.getId() == null ? null : action.getId().value();
        return new CorrectiveActionJpaEntity(
                id, action.getSalinityAlertId(), action.getActionType(), action.getExecutedAt(),
                action.getNotes().orElse(null), action.getRegisteredBy(), action.getRegisteredAt());
    }
}
