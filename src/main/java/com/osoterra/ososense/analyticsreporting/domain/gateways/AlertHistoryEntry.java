package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.time.LocalDateTime;

public record AlertHistoryEntry(String severity, String status, LocalDateTime generatedAt) {
}
