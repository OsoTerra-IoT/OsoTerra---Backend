package com.osoterra.ososense.identityaccess.domain.services;

public record AuthenticateUserCommand(String email, String rawPassword) {
}
