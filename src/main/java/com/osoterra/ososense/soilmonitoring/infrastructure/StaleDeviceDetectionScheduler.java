package com.osoterra.ososense.soilmonitoring.infrastructure;

import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommand;
import com.osoterra.ososense.farmmanagement.domain.services.MarkDeviceOfflineCommandService;
import com.osoterra.ososense.soilmonitoring.domain.gateways.DeviceLocationLookup;
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
 * codebase. A device counts as quiet after two of its own reading intervals without a
 * reading (30 minutes with the firmware's 15-minute interval); its next reading brings it
 * back online.
 */
@Component
public class StaleDeviceDetectionScheduler {

    private static final int MISSED_READINGS = 2;
    private static final int DEFAULT_INTERVAL_MINUTES = 15;

    private final SoilReadingRepository soilReadingRepository;
    private final MarkDeviceOfflineCommandService markDeviceOfflineCommandService;
    private final DeviceLocationLookup deviceLocationLookup;

    public StaleDeviceDetectionScheduler(
            SoilReadingRepository soilReadingRepository,
            MarkDeviceOfflineCommandService markDeviceOfflineCommandService,
            DeviceLocationLookup deviceLocationLookup) {
        this.soilReadingRepository = soilReadingRepository;
        this.markDeviceOfflineCommandService = markDeviceOfflineCommandService;
        this.deviceLocationLookup = deviceLocationLookup;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void detectStaleDevices() {
        LocalDateTime now = LocalDateTime.now();
        for (Long deviceId : soilReadingRepository.findDistinctDeviceIds()) {
            int interval = deviceLocationLookup.findReadingIntervalMinutes(deviceId).orElse(DEFAULT_INTERVAL_MINUTES);
            LocalDateTime threshold = now.minus(Duration.ofMinutes((long) MISSED_READINGS * interval));
            soilReadingRepository
                    .findTopByDeviceIdOrderByCapturedAtDesc(deviceId)
                    .filter(reading -> reading.getCapturedAt().isBefore(threshold))
                    .ifPresent(reading -> markDeviceOfflineCommandService.handle(new MarkDeviceOfflineCommand(deviceId)));
        }
    }
}
