package com.osoterra.ososense.salinityalerting.interfaces.rest.controllers;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationPreference;
import com.osoterra.ososense.salinityalerting.domain.services.SalinityAlertingQueryService;
import com.osoterra.ososense.salinityalerting.domain.services.SetNotificationPreferenceCommand;
import com.osoterra.ososense.salinityalerting.domain.services.SetNotificationPreferenceCommandService;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.NotificationPreferenceResource;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.NotificationPreferenceResourceAssembler;
import com.osoterra.ososense.salinityalerting.interfaces.rest.resources.SetNotificationPreferenceResource;
import com.osoterra.ososense.shared.interfaces.rest.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification-preferences")
class NotificationPreferenceController {

    private final SetNotificationPreferenceCommandService setNotificationPreferenceCommandService;
    private final SalinityAlertingQueryService salinityAlertingQueryService;
    private final NotificationPreferenceResourceAssembler notificationPreferenceResourceAssembler;

    NotificationPreferenceController(
            SetNotificationPreferenceCommandService setNotificationPreferenceCommandService,
            SalinityAlertingQueryService salinityAlertingQueryService,
            NotificationPreferenceResourceAssembler notificationPreferenceResourceAssembler) {
        this.setNotificationPreferenceCommandService = setNotificationPreferenceCommandService;
        this.salinityAlertingQueryService = salinityAlertingQueryService;
        this.notificationPreferenceResourceAssembler = notificationPreferenceResourceAssembler;
    }

    @GetMapping("/mine")
    NotificationPreferenceResource mine(@CurrentUserId Long userId) {
        NotificationPreference preference = salinityAlertingQueryService
                .findNotificationPreferenceByUserAccountId(userId)
                .orElseGet(() -> NotificationPreference.defaultFor(userId));
        return notificationPreferenceResourceAssembler.toResource(preference);
    }

    @PutMapping("/mine")
    NotificationPreferenceResource setMine(
            @Valid @RequestBody SetNotificationPreferenceResource request, @CurrentUserId Long userId) {
        NotificationPreference preference = setNotificationPreferenceCommandService.handle(
                new SetNotificationPreferenceCommand(
                        userId, request.minimumSeverity(), request.channel(), request.pushDeviceToken()));
        return notificationPreferenceResourceAssembler.toResource(preference);
    }
}
