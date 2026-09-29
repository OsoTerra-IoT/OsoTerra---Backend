package com.osoterra.ososense.analyticsreporting.interfaces.rest.controllers;

import com.osoterra.ososense.analyticsreporting.domain.model.PlotReport;
import com.osoterra.ososense.analyticsreporting.domain.model.PlotReportId;
import com.osoterra.ososense.analyticsreporting.domain.services.AnalyticsReportingQueryService;
import com.osoterra.ososense.analyticsreporting.domain.services.GeneratePlotReportCommand;
import com.osoterra.ososense.analyticsreporting.domain.services.GeneratePlotReportCommandService;
import com.osoterra.ososense.analyticsreporting.infrastructure.PdfReportExporter;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.GeneratePlotReportResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.PlotReportResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.PlotReportResourceAssembler;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.ReportSectionResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.ReportSectionResourceAssembler;
import com.osoterra.ososense.shared.EntityNotFoundException;
import com.osoterra.ososense.shared.web.CurrentUserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plot-reports")
class PlotReportController {

    private final GeneratePlotReportCommandService generatePlotReportCommandService;
    private final AnalyticsReportingQueryService analyticsReportingQueryService;
    private final PdfReportExporter pdfReportExporter;
    private final PlotReportResourceAssembler plotReportResourceAssembler;
    private final ReportSectionResourceAssembler reportSectionResourceAssembler;

    PlotReportController(
            GeneratePlotReportCommandService generatePlotReportCommandService,
            AnalyticsReportingQueryService analyticsReportingQueryService,
            PdfReportExporter pdfReportExporter,
            PlotReportResourceAssembler plotReportResourceAssembler,
            ReportSectionResourceAssembler reportSectionResourceAssembler) {
        this.generatePlotReportCommandService = generatePlotReportCommandService;
        this.analyticsReportingQueryService = analyticsReportingQueryService;
        this.pdfReportExporter = pdfReportExporter;
        this.plotReportResourceAssembler = plotReportResourceAssembler;
        this.reportSectionResourceAssembler = reportSectionResourceAssembler;
    }

    @PostMapping
    ResponseEntity<PlotReportResource> generate(
            @Valid @RequestBody GeneratePlotReportResource request, @CurrentUserId Long userId) {
        PlotReport report = generatePlotReportCommandService.handle(new GeneratePlotReportCommand(
                request.plotId(), userId, request.periodStart(), request.periodEnd()));
        return ResponseEntity.status(HttpStatus.CREATED).body(plotReportResourceAssembler.toResource(report));
    }

    @GetMapping
    List<PlotReportResource> byPlot(@RequestParam Long plotId) {
        return analyticsReportingQueryService.findReportsByPlotId(plotId).stream()
                .map(plotReportResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}/sections")
    List<ReportSectionResource> sections(@PathVariable Long id) {
        return analyticsReportingQueryService.findSectionsByReportId(new PlotReportId(id)).stream()
                .map(reportSectionResourceAssembler::toResource)
                .toList();
    }

    @GetMapping(value = "/{id}/export", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<byte[]> export(@PathVariable Long id) {
        PlotReportId reportId = new PlotReportId(id);
        PlotReport report = analyticsReportingQueryService
                .findReportById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Plot report not found for id " + id));
        var sections = analyticsReportingQueryService.findSectionsByReportId(reportId);
        byte[] pdf = pdfReportExporter.export(report, sections);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdf);
    }
}
