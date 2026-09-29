package com.osoterra.ososense.analyticsreporting.domain.gateways;

import java.time.LocalDate;
import java.util.List;

/**
 * Resolves the compensated-conductivity time series for a plot over a period.
 * Implemented by an anti-corruption layer client into Soil Monitoring.
 */
public interface SoilReadingSeriesLookup {

    List<SeriesPoint> findSeriesForPlot(Long plotId, LocalDate periodStart, LocalDate periodEnd);
}
