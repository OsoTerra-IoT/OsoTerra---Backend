package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import java.time.LocalDateTime;

public record FarmResource(
        Long id, Long ownerId, String name, String department, String province, String district,
        LocalDateTime createdAt) {
}
