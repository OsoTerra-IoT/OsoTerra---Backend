package com.osoterra.ososense.salinityalerting.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record SetNotificationPreferenceResource(
        @NotBlank String minimumSeverity, @NotBlank String channel, String pushDeviceToken) {
}
