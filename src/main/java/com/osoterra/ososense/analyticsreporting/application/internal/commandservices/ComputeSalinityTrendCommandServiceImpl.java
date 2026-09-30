package com.osoterra.ososense.analyticsreporting.application.internal.commandservices;

import com.osoterra.ososense.analyticsreporting.domain.gateways.SeriesPoint;
import com.osoterra.ososense.analyticsreporting.domain.gateways.SoilReadingSeriesLookup;
import com.osoterra.ososense.analyticsreporting.domain.model.SalinityTrend;
import com.osoterra.ososense.analyticsreporting.domain.repositories.SalinityTrendRepository;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommand;
import com.osoterra.ososense.analyticsreporting.domain.services.ComputeSalinityTrendCommandService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * Computes the linear trend of a plot's compensated conductivity over a period via
 * ordinary least squares regression against the day offset from the period start.
 */
@Service
class ComputeSalinityTrendCommandServiceImpl implements ComputeSalinityTrendCommandService {

    private final SoilReadingSeriesLookup soilReadingSeriesLookup;
    private final SalinityTrendRepository salinityTrendRepository;

    ComputeSalinityTrendCommandServiceImpl(
            SoilReadingSeriesLookup soilReadingSeriesLookup, SalinityTrendRepository salinityTrendRepository) {
        this.soilReadingSeriesLookup = soilReadingSeriesLookup;
        this.salinityTrendRepository = salinityTrendRepository;
    }

    @Override
    public SalinityTrend handle(ComputeSalinityTrendCommand command) {
        List<SeriesPoint> series = soilReadingSeriesLookup.findSeriesForPlot(
                command.plotId(), command.periodStart(), command.periodEnd());
        BigDecimal slope = ordinaryLeastSquaresSlope(series, command.periodStart());
        SalinityTrend trend = SalinityTrend.compute(
                command.plotId(), command.periodStart(), command.periodEnd(), slope, series.size());
        return salinityTrendRepository.save(trend);
    }

    private BigDecimal ordinaryLeastSquaresSlope(List<SeriesPoint> series, LocalDate periodStart) {
        int n = series.size();
        if (n == 0) {
            return BigDecimal.ZERO;
        }
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumXX = 0;
        for (SeriesPoint point : series) {
            double x = Duration.between(periodStart.atStartOfDay(), point.capturedAt()).toHours() / 24.0;
            double y = point.compensatedConductivityDsM().doubleValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }
        double meanX = sumX / n;
        double meanY = sumY / n;
        double numerator = sumXY - (double) n * meanX * meanY;
        double denominator = sumXX - (double) n * meanX * meanX;
        double slope = denominator == 0 ? 0 : numerator / denominator;
        return BigDecimal.valueOf(slope).setScale(5, RoundingMode.HALF_UP);
    }
}
