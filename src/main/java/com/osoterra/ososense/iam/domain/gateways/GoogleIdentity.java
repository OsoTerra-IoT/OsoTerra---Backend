package com.osoterra.ososense.iam.domain.gateways;

public record GoogleIdentity(String email, String subjectId, String firstName, String lastName) {
}
