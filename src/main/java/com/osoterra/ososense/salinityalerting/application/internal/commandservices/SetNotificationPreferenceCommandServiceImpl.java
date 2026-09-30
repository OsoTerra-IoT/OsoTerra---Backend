package com.osoterra.ososense.salinityalerting.application.internal.commandservices;

import com.osoterra.ososense.salinityalerting.domain.model.AlertSeverity;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.repositories.NotificationPreferenceRepository;
import com.osoterra.ososense.salinityalerting.domain.services.SetNotificationPreferenceCommand;
import com.osoterra.ososense.salinityalerting.domain.services.SetNotificationPreferenceCommandService;
import org.springframework.stereotype.Service;

@Service
class SetNotificationPreferenceCommandServiceImpl implements SetNotificationPreferenceCommandService {

    private final NotificationPreferenceRepository notificationPreferenceRepository;

    SetNotificationPreferenceCommandServiceImpl(NotificationPreferenceRepository notificationPreferenceRepository) {
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }

    @Override
    public NotificationPreference handle(SetNotificationPreferenceCommand command) {
        NotificationPreference preference = notificationPreferenceRepository
                .findByUserAccountId(command.userAccountId())
                .orElseGet(() -> NotificationPreference.register(
                        command.userAccountId(), NotificationChannel.valueOf(command.channel()),
                        command.pushDeviceToken()));
        preference.update(
                AlertSeverity.valueOf(command.minimumSeverity()), NotificationChannel.valueOf(command.channel()),
                command.pushDeviceToken());
        return notificationPreferenceRepository.save(preference);
    }
}
