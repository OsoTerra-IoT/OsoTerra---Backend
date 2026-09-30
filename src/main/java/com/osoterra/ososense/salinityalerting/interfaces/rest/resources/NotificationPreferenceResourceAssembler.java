package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import org.springframework.stereotype.Component;

@Component
public class NotificationPreferenceResourceAssembler {

    public NotificationPreferenceResource toResource(NotificationPreference preference) {
        Long id = preference.getId() == null ? null : preference.getId().value();
        return new NotificationPreferenceResource(
                id,
                preference.getUserAccountId(),
                preference.getMinimumSeverity().name(),
                preference.getChannel().name(),
                preference.getPushDeviceToken().orElse(null),
                preference.getPreferredLanguage());
    }
}
