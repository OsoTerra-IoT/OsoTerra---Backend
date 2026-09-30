package com.osoterra.ososense.soilmonitoring.interfaces.rest.controllers;

import com.osoterra.ososense.soilmonitoring.domain.services.SoilMonitoringQueryService;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.SoilReadingResource;
import com.osoterra.ososense.soilmonitoring.interfaces.rest.resources.SoilReadingResourceAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/soil-readings")
class SoilReadingController {

    private final SoilMonitoringQueryService soilMonitoringQueryService;
    private final SoilReadingResourceAssembler soilReadingResourceAssembler;

    SoilReadingController(
            SoilMonitoringQueryService soilMonitoringQueryService,
            SoilReadingResourceAssembler soilReadingResourceAssembler) {
        this.soilMonitoringQueryService = soilMonitoringQueryService;
        this.soilReadingResourceAssembler = soilReadingResourceAssembler;
    }

    @GetMapping
    List<SoilReadingResource> byPlot(@RequestParam Long plotId) {
        return soilMonitoringQueryService.findReadingsByPlotId(plotId).stream()
                .map(soilReadingResourceAssembler::toResource)
                .toList();
    }
}
