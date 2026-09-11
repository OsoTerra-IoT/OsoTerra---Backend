package com.osoterra.ososense.identityaccess.infrastructure.integration;

import com.osoterra.ososense.identityaccess.domain.gateways.PasswordResetNotifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Anti-corruption layer that translates a password reset request into a message for the
 * SMTP provider, keeping that vocabulary out of the domain.
 */
@Component
class SmtpPasswordResetNotifier implements PasswordResetNotifier {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    SmtpPasswordResetNotifier(
            JavaMailSender mailSender, @Value("${app.mail.from:no-reply@osoterra.com}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void notifyResetRequested(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject("OsoTerra IoT - Password reset");
        message.setText("Use the following code to reset your password: " + token
                + "\nThis code expires in one hour. If you did not request this, ignore this email.");
        mailSender.send(message);
    }
}
