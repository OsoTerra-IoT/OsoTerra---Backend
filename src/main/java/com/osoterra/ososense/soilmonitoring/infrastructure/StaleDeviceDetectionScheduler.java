package com.osoterra.ososense.soilmonitoring.infrastructure;

import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommand;
import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommandService;
import com.osoterra.ososense.soilmonitoring.domain.repositories.SoilReadingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Marks a device offline in Farm Management once it has gone quiet for longer than the
 * expected reading interval — a direct cross-module call standing in for the
 * event-driven {@code DeviceWentOfflineEvent} the architecture describes, for the same
 * reason as {@code EvaluateReadingCommandService}: no event bus exists yet in this
 * codebase. The two-hour threshold is a placeholder default; a real deployment would
 * derive it from each device's own configured reading interval.
 */
@Component
public class StaleDeviceDetectionScheduler {

    private static final Duration STALE_THRESHOLD = Duration.ofHours(2);

    private final SoilReadingRepository soilReadingRepository;
    private final MarkDeviceOfflineCommandService markDeviceOfflineCommandService;

    public StaleDeviceDetectionScheduler(
            SoilReadingRepository soilReadingRepository,
            MarkDeviceOfflineCommandService markDeviceOfflineCommandService) {
        this.soilReadingRepository = soilReadingRepository;
        this.markDeviceOfflineCommandService = markDeviceOfflineCommandService;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void detectStaleDevices() {
        LocalDateTime threshold = LocalDateTime.now().minus(STALE_THRESHOLD);
        for (Long deviceId : soilReadingRepository.findDistinctDeviceIds()) {
            soilReadingRepository
                    .findTopByDeviceIdOrderByCapturedAtDesc(deviceId)
                    .filter(reading -> reading.getCapturedAt().isBefore(threshold))
                    .ifPresent(reading -> markDeviceOfflineCommandService.handle(new MarkDeviceOfflineCommand(deviceId)));
        }
    }
}
