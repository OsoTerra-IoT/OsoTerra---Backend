package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.farmmanagement.domain.events.DeviceInstalledInPlotEvent;
import com.osoterra.ososense.shared.AggregateRoot;
import com.osoterra.ososense.shared.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a soil sensor device. A device is factory-provisioned with an
 * activation code and starts {@link DeviceStatus#UNASSIGNED} until a farmer attaches it
 * to a plot.
 */
public final class Device extends AggregateRoot<DeviceId> {

    private static final int DEFAULT_READING_INTERVAL_MINUTES = 30;
    private static final BigDecimal DEFAULT_CALIBRATION_FACTOR = BigDecimal.ONE;

    private PlotId plotId;
    private final String activationCode;
    private DeviceStatus status;
    private final BigDecimal calibrationFactor;
    private final Integer batteryLevel;
    private final String firmwareVersion;
    private final int readingIntervalMinutes;
    private final LocalDateTime lastSeenAt;
    private final LocalDateTime createdAt;

    private Device(
            DeviceId id, PlotId plotId, String activationCode, DeviceStatus status, BigDecimal calibrationFactor,
            Integer batteryLevel, String firmwareVersion, int readingIntervalMinutes, LocalDateTime lastSeenAt,
            LocalDateTime createdAt) {
        super(id);
        this.plotId = plotId;
        this.activationCode = activationCode;
        this.status = status;
        this.calibrationFactor = calibrationFactor;
        this.batteryLevel = batteryLevel;
        this.firmwareVersion = firmwareVersion;
        this.readingIntervalMinutes = readingIntervalMinutes;
        this.lastSeenAt = lastSeenAt;
        this.createdAt = createdAt;
    }

    public static Device registerWithActivationCode(String activationCode) {
        Objects.requireNonNull(activationCode, "activationCode");
        if (activationCode.isBlank()) {
            throw new IllegalArgumentException("A device's activation code must not be blank");
        }
        return new Device(
                null, null, activationCode, DeviceStatus.UNASSIGNED, DEFAULT_CALIBRATION_FACTOR, null, null,
                DEFAULT_READING_INTERVAL_MINUTES, null, LocalDateTime.now());
    }

    public static Device reconstruct(
            DeviceId id, PlotId plotId, String activationCode, DeviceStatus status, BigDecimal calibrationFactor,
            Integer batteryLevel, String firmwareVersion, int readingIntervalMinutes, LocalDateTime lastSeenAt,
            LocalDateTime createdAt) {
        return new Device(
                id, plotId, activationCode, status, calibrationFactor, batteryLevel, firmwareVersion,
                readingIntervalMinutes, lastSeenAt, createdAt);
    }

    public void attachToPlot(PlotId plotId) {
        Objects.requireNonNull(plotId, "plotId");
        if (status != DeviceStatus.UNASSIGNED) {
            throw new BusinessRuleViolationException("Only an unassigned device can be attached to a plot");
        }
        this.plotId = plotId;
        this.status = DeviceStatus.ACTIVE;
        registerEvent(new DeviceInstalledInPlotEvent(getId(), plotId, Instant.now()));
    }

    public void markOffline() {
        if (status == DeviceStatus.ACTIVE) {
            this.status = DeviceStatus.OFFLINE;
        }
    }

    public Optional<PlotId> getPlotId() {
        return Optional.ofNullable(plotId);
    }

    public String getActivationCode() {
        return activationCode;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public BigDecimal getCalibrationFactor() {
        return calibrationFactor;
    }

    public Optional<Integer> getBatteryLevel() {
        return Optional.ofNullable(batteryLevel);
    }

    public Optional<String> getFirmwareVersion() {
        return Optional.ofNullable(firmwareVersion);
    }

    public int getReadingIntervalMinutes() {
        return readingIntervalMinutes;
    }

    public Optional<LocalDateTime> getLastSeenAt() {
        return Optional.ofNullable(lastSeenAt);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
