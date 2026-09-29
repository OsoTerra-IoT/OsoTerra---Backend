package com.osoterra.ososense.salinityalerting.infrastructure.integration;

import com.osoterra.ososense.salinityalerting.domain.gateways.NotificationDispatcher;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Placeholder push notification delivery. The report names a generic "push/email
 * provider" without picking a vendor (Firebase Cloud Messaging, APNs, etc.) — until the
 * team chooses one, this logs what would have been sent instead of calling a real
 * provider, so the notification flow can still be exercised end-to-end.
 */
@Component
class PushNotificationDispatcher implements NotificationDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationDispatcher.class);

    @Override
    public NotificationChannel supportedChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    public void dispatch(SalinityAlert alert, Long recipientUserAccountId, String recipientPushToken) {
        if (recipientPushToken == null) {
            return;
        }
        LOGGER.info(
                "Push notification (no provider configured yet) — token={}, severity={}, plotId={}",
                recipientPushToken, alert.getSeverity(), alert.getPlotId());
    }
}
