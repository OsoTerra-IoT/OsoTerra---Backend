package com.osoterra.ososense.salinityalerting.domain.services;

public record AcknowledgeAlertCommand(Long alertId, Long byUserId) {
}
