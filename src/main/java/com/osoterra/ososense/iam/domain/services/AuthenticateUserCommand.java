package com.osoterra.ososense.iam.domain.services;

public record AuthenticateUserCommand(String email, String rawPassword) {
}
