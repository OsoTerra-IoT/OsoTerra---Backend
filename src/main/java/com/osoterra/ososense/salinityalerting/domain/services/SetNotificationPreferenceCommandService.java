package com.osoterra.ososense.salinityalerting.domain.services;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;

public interface SetNotificationPreferenceCommandService {

    NotificationPreference handle(SetNotificationPreferenceCommand command);
}
