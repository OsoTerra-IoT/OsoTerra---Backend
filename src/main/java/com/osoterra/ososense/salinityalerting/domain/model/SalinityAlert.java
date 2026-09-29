package com.osoterra.ososense.salinityalerting.domain.model;

import com.osoterra.ososense.salinityalerting.domain.events.AlertGeneratedEvent;
import com.osoterra.ososense.shared.AggregateRoot;
import com.osoterra.ososense.shared.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate root for a salinity alert. Severity is derived from how far the observed
 * conductivity exceeds the crop's threshold as a ratio, not from the absolute value —
 * the same excess means something different for a tolerant crop than for a sensitive one.
 */
public final class SalinityAlert extends AggregateRoot<SalinityAlertId> {

    private static final BigDecimal WARNING_RATIO = new BigDecimal("1.2");
    private static final BigDecimal CRITICAL_RATIO = new BigDecimal("1.5");

    private final Long plotId;
    private final Long soilReadingId;
    private final BigDecimal observedConductivityDsM;
    private final BigDecimal appliedThresholdDsM;
    private final BigDecimal excessRatio;
    private final AlertSeverity severity;
    private AlertStatus status;
    private Long acknowledgedBy;
    private LocalDateTime acknowledgedAt;
    private final LocalDateTime generatedAt;

    private SalinityAlert(
            SalinityAlertId id, Long plotId, Long soilReadingId, BigDecimal observedConductivityDsM,
            BigDecimal appliedThresholdDsM, BigDecimal excessRatio, AlertSeverity severity, AlertStatus status,
            Long acknowledgedBy, LocalDateTime acknowledgedAt, LocalDateTime generatedAt) {
        super(id);
        this.plotId = plotId;
        this.soilReadingId = soilReadingId;
        this.observedConductivityDsM = observedConductivityDsM;
        this.appliedThresholdDsM = appliedThresholdDsM;
        this.excessRatio = excessRatio;
        this.severity = severity;
        this.status = status;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = acknowledgedAt;
        this.generatedAt = generatedAt;
    }

    public static SalinityAlert generate(
            Long plotId, Long soilReadingId, BigDecimal observedConductivityDsM, BigDecimal appliedThresholdDsM) {
        Objects.requireNonNull(plotId, "plotId");
        Objects.requireNonNull(soilReadingId, "soilReadingId");
        Objects.requireNonNull(observedConductivityDsM, "observedConductivityDsM");
        Objects.requireNonNull(appliedThresholdDsM, "appliedThresholdDsM");
        if (observedConductivityDsM.compareTo(appliedThresholdDsM) <= 0) {
            throw new IllegalArgumentException("No alert is warranted when the observed value does not exceed the threshold");
        }
        BigDecimal excessRatio = observedConductivityDsM.divide(appliedThresholdDsM, 4, RoundingMode.HALF_UP);
        AlertSeverity severity = severityFor(excessRatio);
        SalinityAlert alert = new SalinityAlert(
                null, plotId, soilReadingId, observedConductivityDsM, appliedThresholdDsM, excessRatio, severity,
                AlertStatus.OPEN, null, null, LocalDateTime.now());
        alert.registerEvent(new AlertGeneratedEvent(alert.getId(), plotId, severity, Instant.now()));
        return alert;
    }

    private static AlertSeverity severityFor(BigDecimal excessRatio) {
        if (excessRatio.compareTo(WARNING_RATIO) < 0) {
            return AlertSeverity.WATCH;
        }
        if (excessRatio.compareTo(CRITICAL_RATIO) < 0) {
            return AlertSeverity.WARNING;
        }
        return AlertSeverity.CRITICAL;
    }

    public static SalinityAlert reconstruct(
            SalinityAlertId id, Long plotId, Long soilReadingId, BigDecimal observedConductivityDsM,
            BigDecimal appliedThresholdDsM, BigDecimal excessRatio, AlertSeverity severity, AlertStatus status,
            Long acknowledgedBy, LocalDateTime acknowledgedAt, LocalDateTime generatedAt) {
        return new SalinityAlert(
                id, plotId, soilReadingId, observedConductivityDsM, appliedThresholdDsM, excessRatio, severity,
                status, acknowledgedBy, acknowledgedAt, generatedAt);
    }

    public void acknowledge(Long byUserId) {
        if (status != AlertStatus.OPEN) {
            throw new BusinessRuleViolationException("Only an open alert can be acknowledged");
        }
        this.status = AlertStatus.ACKNOWLEDGED;
        this.acknowledgedBy = byUserId;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public void resolve() {
        if (status == AlertStatus.RESOLVED) {
            throw new BusinessRuleViolationException("The alert is already resolved");
        }
        this.status = AlertStatus.RESOLVED;
    }

    public Long getPlotId() {
        return plotId;
    }

    public Long getSoilReadingId() {
        return soilReadingId;
    }

    public BigDecimal getObservedConductivityDsM() {
        return observedConductivityDsM;
    }

    public BigDecimal getAppliedThresholdDsM() {
        return appliedThresholdDsM;
    }

    public BigDecimal getExcessRatio() {
        return excessRatio;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public Optional<Long> getAcknowledgedBy() {
        return Optional.ofNullable(acknowledgedBy);
    }

    public Optional<LocalDateTime> getAcknowledgedAt() {
        return Optional.ofNullable(acknowledgedAt);
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
