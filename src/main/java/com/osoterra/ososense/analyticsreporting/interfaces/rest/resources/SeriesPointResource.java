package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SeriesPointResource(LocalDateTime capturedAt, BigDecimal compensatedConductivityDsM) {
}
