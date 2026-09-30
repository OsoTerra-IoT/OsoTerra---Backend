package com.osoterra.ososense.salinityalerting.infrastructure.integration;

import com.osoterra.ososense.iam.domain.model.UserAccount;
import com.osoterra.ososense.iam.domain.model.UserAccountId;
import com.osoterra.ososense.iam.domain.services.UserAccountQueryService;
import com.osoterra.ososense.salinityalerting.domain.gateways.NotificationDispatcher;
import com.osoterra.ososense.salinityalerting.domain.model.NotificationChannel;
import com.osoterra.ososense.salinityalerting.domain.model.SalinityAlert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Delivers an alert notification by email. The recipient's address is resolved directly
 * from IAM's query service — a cross-module lookup at the infrastructure layer, kept out
 * of the domain, since this is purely a delivery-mechanism concern.
 */
@Component
class EmailNotificationDispatcher implements NotificationDispatcher {

    private final JavaMailSender mailSender;
    private final UserAccountQueryService userAccountQueryService;
    private final String fromAddress;

    EmailNotificationDispatcher(
            JavaMailSender mailSender, UserAccountQueryService userAccountQueryService,
            @Value("${app.mail.from:no-reply@osoterra.com}") String fromAddress) {
        this.mailSender = mailSender;
        this.userAccountQueryService = userAccountQueryService;
        this.fromAddress = fromAddress;
    }

    @Override
    public NotificationChannel supportedChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void dispatch(SalinityAlert alert, Long recipientUserAccountId, String recipientPushToken) {
        userAccountQueryService.findById(new UserAccountId(recipientUserAccountId))
                .ifPresent(account -> sendFor(account, alert));
    }

    private void sendFor(UserAccount account, SalinityAlert alert) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(account.getEmail().value());
        message.setSubject("OsoTerra IoT - Salinity alert (" + alert.getSeverity() + ")");
        message.setText("A " + alert.getSeverity() + " salinity alert was generated for plot #" + alert.getPlotId()
                + ". Sign in to the app to review it and register a corrective action.");
        mailSender.send(message);
    }
}
