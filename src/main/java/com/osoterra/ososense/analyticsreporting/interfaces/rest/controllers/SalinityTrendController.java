package com.osoterra.ososense.analyticsreporting.interfaces.rest.controllers;

import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.services.AnalyticsReportingQueryService;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommand;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommandService;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.ComputeSalinityTrendResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.SalinityTrendResource;
import com.osoterra.ososense.analyticsreporting.interfaces.rest.resources.SalinityTrendResourceAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salinity-trends")
class SalinityTrendController {

    private final ComputeSalinityTrendCommandService computeSalinityTrendCommandService;
    private final AnalyticsReportingQueryService analyticsReportingQueryService;
    private final SalinityTrendResourceAssembler salinityTrendResourceAssembler;

    SalinityTrendController(
            ComputeSalinityTrendCommandService computeSalinityTrendCommandService,
            AnalyticsReportingQueryService analyticsReportingQueryService,
            SalinityTrendResourceAssembler salinityTrendResourceAssembler) {
        this.computeSalinityTrendCommandService = computeSalinityTrendCommandService;
        this.analyticsReportingQueryService = analyticsReportingQueryService;
        this.salinityTrendResourceAssembler = salinityTrendResourceAssembler;
    }

    @PostMapping
    ResponseEntity<SalinityTrendResource> compute(@Valid @RequestBody ComputeSalinityTrendResource request) {
        SalinityTrend trend = computeSalinityTrendCommandService.handle(
                new ComputeSalinityTrendCommand(request.plotId(), request.periodStart(), request.periodEnd()));
        return ResponseEntity.status(HttpStatus.CREATED).body(salinityTrendResourceAssembler.toResource(trend));
    }

    @GetMapping
    List<SalinityTrendResource> byPlot(@RequestParam Long plotId) {
        return analyticsReportingQueryService.findTrendsByPlotId(plotId).stream()
                .map(salinityTrendResourceAssembler::toResource)
                .toList();
    }
}
