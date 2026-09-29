package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AlertSummaryResource(String severity, String status, LocalDateTime generatedAt) {
}
