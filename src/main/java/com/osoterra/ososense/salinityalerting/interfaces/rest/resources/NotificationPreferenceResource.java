package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

public record NotificationPreferenceResource(
        Long id, Long userAccountId, String minimumSeverity, String channel, String pushDeviceToken,
        String preferredLanguage) {
}
