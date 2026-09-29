package com.osoterra.ososense.analyticsreporting.domain.model;

import com.osoterra.ososense.shared.AggregateRoot;

import java.util.Objects;

/**
 * Aggregate root for one section of content within a {@link PlotReport}, referenced by
 * the report's id only — not a JPA association — consistent with how every other
 * cross-aggregate reference in this codebase is modeled.
 */
public final class ReportSection extends AggregateRoot<ReportSectionId> {

    private final PlotReportId plotReportId;
    private final String title;
    private final String sectionType;
    private final String content;
    private final int displayOrder;

    private ReportSection(
            ReportSectionId id, PlotReportId plotReportId, String title, String sectionType, String content,
            int displayOrder) {
        super(id);
        this.plotReportId = plotReportId;
        this.title = title;
        this.sectionType = sectionType;
        this.content = content;
        this.displayOrder = displayOrder;
    }

    public static ReportSection create(
            PlotReportId plotReportId, String title, String sectionType, String content, int displayOrder) {
        Objects.requireNonNull(plotReportId, "plotReportId");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(content, "content");
        return new ReportSection(null, plotReportId, title, sectionType, content, displayOrder);
    }

    public static ReportSection reconstruct(
            ReportSectionId id, PlotReportId plotReportId, String title, String sectionType, String content,
            int displayOrder) {
        return new ReportSection(id, plotReportId, title, sectionType, content, displayOrder);
    }

    public PlotReportId getPlotReportId() {
        return plotReportId;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionType() {
        return sectionType;
    }

    public String getContent() {
        return content;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
