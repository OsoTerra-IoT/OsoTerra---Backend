package com.osoterra.ososense.salinityalerting.domain.services;

import java.math.BigDecimal;

public record EvaluateReadingCommand(Long plotId, Long soilReadingId, BigDecimal observedConductivityDsM) {
}
