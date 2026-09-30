package com.osoterra.ososense.analyticsreporting.domain.model;

import com.osoterra.ososense.shared.domain.model.AggregateRoot;
import com.osoterra.ososense.shared.domain.exceptions.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root for a computed salinity trend over a plot's reading history. A trend
 * is only considered reliable with at least 30 readings in the period.
 */
public final class SalinityTrend extends AggregateRoot<SalinityTrendId> {

    private static final int MINIMUM_READING_COUNT = 30;
    private static final BigDecimal STABLE_TOLERANCE = new BigDecimal("0.001");

    private final Long plotId;
    private final LocalDate periodStartDate;
    private final LocalDate periodEndDate;
    private final BigDecimal slopeDsMPerDay;
    private final TrendDirection direction;
    private final int readingCount;
    private final LocalDateTime computedAt;

    private SalinityTrend(
            SalinityTrendId id, Long plotId, LocalDate periodStartDate, LocalDate periodEndDate,
            BigDecimal slopeDsMPerDay, TrendDirection direction, int readingCount, LocalDateTime computedAt) {
        super(id);
        this.plotId = plotId;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.slopeDsMPerDay = slopeDsMPerDay;
        this.direction = direction;
        this.readingCount = readingCount;
        this.computedAt = computedAt;
    }

    public static SalinityTrend compute(
            Long plotId, LocalDate periodStartDate, LocalDate periodEndDate, BigDecimal slopeDsMPerDay,
            int readingCount) {
        Objects.requireNonNull(plotId, "plotId");
        Objects.requireNonNull(periodStartDate, "periodStartDate");
        Objects.requireNonNull(periodEndDate, "periodEndDate");
        Objects.requireNonNull(slopeDsMPerDay, "slopeDsMPerDay");
        if (!periodEndDate.isAfter(periodStartDate)) {
            throw new IllegalArgumentException("The trend period end date must be after its start date");
        }
        if (readingCount < MINIMUM_READING_COUNT) {
            throw new BusinessRuleViolationException(
                    "At least " + MINIMUM_READING_COUNT + " readings are required to compute a reliable trend");
        }
        return new SalinityTrend(
                null, plotId, periodStartDate, periodEndDate, slopeDsMPerDay, directionFor(slopeDsMPerDay),
                readingCount, LocalDateTime.now());
    }

    private static TrendDirection directionFor(BigDecimal slope) {
        if (slope.abs().compareTo(STABLE_TOLERANCE) < 0) {
            return TrendDirection.STABLE;
        }
        return slope.signum() > 0 ? TrendDirection.RISING : TrendDirection.FALLING;
    }

    public static SalinityTrend reconstruct(
            SalinityTrendId id, Long plotId, LocalDate periodStartDate, LocalDate periodEndDate,
            BigDecimal slopeDsMPerDay, TrendDirection direction, int readingCount, LocalDateTime computedAt) {
        return new SalinityTrend(
                id, plotId, periodStartDate, periodEndDate, slopeDsMPerDay, direction, readingCount, computedAt);
    }

    public Long getPlotId() {
        return plotId;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public BigDecimal getSlopeDsMPerDay() {
        return slopeDsMPerDay;
    }

    public TrendDirection getDirection() {
        return direction;
    }

    public int getReadingCount() {
        return readingCount;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }
}
