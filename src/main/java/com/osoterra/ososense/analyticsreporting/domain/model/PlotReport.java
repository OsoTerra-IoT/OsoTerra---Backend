package com.osoterra.ososense.analyticsreporting.domain.model;

import com.osoterra.ososense.shared.AggregateRoot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregate root for a generated plot report. Its content lives in a separate
 * collection of {@link ReportSection}s, looked up by this report's id.
 */
public final class PlotReport extends AggregateRoot<PlotReportId> {

    private final Long plotId;
    private final Long generatedBy;
    private final LocalDate periodStartDate;
    private final LocalDate periodEndDate;
    private final LocalDateTime generatedAt;

    private PlotReport(
            PlotReportId id, Long plotId, Long generatedBy, LocalDate periodStartDate, LocalDate periodEndDate,
            LocalDateTime generatedAt) {
        super(id);
        this.plotId = plotId;
        this.generatedBy = generatedBy;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.generatedAt = generatedAt;
    }

    public static PlotReport generate(Long plotId, Long generatedBy, LocalDate periodStartDate, LocalDate periodEndDate) {
        Objects.requireNonNull(plotId, "plotId");
        Objects.requireNonNull(generatedBy, "generatedBy");
        Objects.requireNonNull(periodStartDate, "periodStartDate");
        Objects.requireNonNull(periodEndDate, "periodEndDate");
        if (!periodEndDate.isAfter(periodStartDate)) {
            throw new IllegalArgumentException("The report period end date must be after its start date");
        }
        return new PlotReport(null, plotId, generatedBy, periodStartDate, periodEndDate, LocalDateTime.now());
    }

    public static PlotReport reconstruct(
            PlotReportId id, Long plotId, Long generatedBy, LocalDate periodStartDate, LocalDate periodEndDate,
            LocalDateTime generatedAt) {
        return new PlotReport(id, plotId, generatedBy, periodStartDate, periodEndDate, generatedAt);
    }

    public Long getPlotId() {
        return plotId;
    }

    public Long getGeneratedBy() {
        return generatedBy;
    }

    public LocalDate getPeriodStartDate() {
        return periodStartDate;
    }

    public LocalDate getPeriodEndDate() {
        return periodEndDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
