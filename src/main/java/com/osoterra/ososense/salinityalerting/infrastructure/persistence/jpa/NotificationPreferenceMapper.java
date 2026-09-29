package com.osoterra.ososense.salinityalerting.infrastructure.persistence.jpa;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreferenceId;

final class NotificationPreferenceMapper {

    private NotificationPreferenceMapper() {
    }

    static NotificationPreference toDomain(NotificationPreferenceJpaEntity entity) {
        return NotificationPreference.reconstruct(
                new NotificationPreferenceId(entity.getId()),
                entity.getUserAccountId(),
                entity.getMinimumSeverity(),
                entity.getChannel(),
                entity.getPushDeviceToken(),
                entity.getPreferredLanguage());
    }

    static NotificationPreferenceJpaEntity toEntity(NotificationPreference preference) {
        Long id = preference.getId() == null ? null : preference.getId().value();
        return new NotificationPreferenceJpaEntity(
                id, preference.getUserAccountId(), preference.getMinimumSeverity(), preference.getChannel(),
                preference.getPushDeviceToken().orElse(null), preference.getPreferredLanguage());
    }
}
