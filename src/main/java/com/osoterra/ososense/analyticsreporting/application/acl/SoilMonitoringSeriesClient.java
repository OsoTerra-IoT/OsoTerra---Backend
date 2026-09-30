package com.osoterra.ososense.analyticsreporting.application.acl;

import com.osoterra.ososense.analyticsreporting.domain.gateways.SeriesPoint;
import com.osoterra.ososense.analyticsreporting.domain.gateways.SoilReadingSeriesLookup;
import com.osoterra.ososense.soilmonitoring.domain.services.SoilMonitoringQueryService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Anti-corruption layer resolving a plot's compensated-conductivity series from Soil
 * Monitoring, translated into this context's own {@link SeriesPoint} shape.
 */
@Component
class SoilMonitoringSeriesClient implements SoilReadingSeriesLookup {

    private final SoilMonitoringQueryService soilMonitoringQueryService;

    SoilMonitoringSeriesClient(SoilMonitoringQueryService soilMonitoringQueryService) {
        this.soilMonitoringQueryService = soilMonitoringQueryService;
    }

    @Override
    public List<SeriesPoint> findSeriesForPlot(Long plotId, LocalDate periodStart, LocalDate periodEnd) {
        return soilMonitoringQueryService
                .findReadingsByPlotIdBetween(plotId, periodStart.atStartOfDay(), periodEnd.atTime(LocalTime.MAX))
                .stream()
                .map(reading -> new SeriesPoint(reading.getCapturedAt(), reading.getCompensatedConductivityDsM()))
                .toList();
    }
}
