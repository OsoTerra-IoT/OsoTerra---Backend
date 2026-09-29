package com.osoterra.ososense.salinityalerting.domain.gateways;

import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;

/**
 * One delivery channel for alert notifications. Spring collects every bean implementing
 * this interface; the caller dispatches through whichever ones match the recipient's
 * preferred channel.
 */
public interface NotificationDispatcher {

    NotificationChannel supportedChannel();

    void dispatch(SalinityAlert alert, Long recipientUserAccountId, String recipientPushToken);
}
