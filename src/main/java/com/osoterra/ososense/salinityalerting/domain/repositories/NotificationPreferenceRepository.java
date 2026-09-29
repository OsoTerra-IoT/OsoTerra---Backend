package com.osoterra.ososense.salinityalerting.domain.repositories;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;

import java.util.Optional;

public interface NotificationPreferenceRepository {

    NotificationPreference save(NotificationPreference preference);

    Optional<NotificationPreference> findByUserAccountId(Long userAccountId);
}
