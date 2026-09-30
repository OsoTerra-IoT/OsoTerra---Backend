package com.osoterra.ososense.farmmanagement.domain.services;

public record RegisterFarmCommand(Long ownerId, String name, String department, String province, String district) {
}
