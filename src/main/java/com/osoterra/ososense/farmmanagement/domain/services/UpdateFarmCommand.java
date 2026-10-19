package com.osoterra.ososense.farmmanagement.domain.services;

public record UpdateFarmCommand(
        Long farmId, Long requestedBy, String name, String department, String province, String district) {
}
