package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SeriesPoint(LocalDateTime capturedAt, BigDecimal compensatedConductivityDsM) {
}
