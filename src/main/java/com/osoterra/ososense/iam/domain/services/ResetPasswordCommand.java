package com.osoterra.ososense.iam.domain.services;

public record ResetPasswordCommand(String token, String newRawPassword) {
}
