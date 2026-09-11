package com.osoterra.ososense.identityaccess.domain.services;

public record ResetPasswordCommand(String token, String newRawPassword) {
}
