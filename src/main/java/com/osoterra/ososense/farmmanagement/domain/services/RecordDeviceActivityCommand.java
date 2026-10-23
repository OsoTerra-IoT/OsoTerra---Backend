package com.osoterra.ososense.farmmanagement.domain.services;

import java.time.LocalDateTime;

public record RecordDeviceActivityCommand(Long deviceId, LocalDateTime capturedAt) {
}
