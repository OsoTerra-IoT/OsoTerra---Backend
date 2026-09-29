package com.osoterra.ososense.salinityalerting.domain.services;

public record SetNotificationPreferenceCommand(
        Long userAccountId, String minimumSeverity, String channel, String pushDeviceToken) {
}
