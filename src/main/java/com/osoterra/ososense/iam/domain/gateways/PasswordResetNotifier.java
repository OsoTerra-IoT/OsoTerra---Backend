package com.osoterra.ososense.iam.domain.gateways;

/**
 * Port for delivering the password reset token to the account owner, without the
 * domain knowing the delivery channel.
 */
public interface PasswordResetNotifier {

    void notifyResetRequested(String email, String token);
}
