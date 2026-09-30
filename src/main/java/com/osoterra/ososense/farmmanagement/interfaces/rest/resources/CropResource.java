package com.osoterra.ososense.farmmanagement.interfaces.rest.resources;

import java.math.BigDecimal;

public record CropResource(
        Long id, String commonName, String scientificName, BigDecimal salinityThresholdDsM,
        String saltToleranceClass, String sourceReference) {
}
