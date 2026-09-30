package com.osoterra.ososense.analyticsreporting.interfaces.rest.resources;

public record ReportSectionResource(
        Long id, Long plotReportId, String title, String sectionType, String content, int displayOrder) {
}
