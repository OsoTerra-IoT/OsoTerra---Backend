package com.osoterra.ososense.farmmanagement.domain.model;

import com.osoterra.ososense.farmmanagement.domain.events.CropAssignedToPlotEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlotTest {

    private static final FarmId FARM_ID = new FarmId(1L);
    private static final GeoCoordinates COORDINATES = new GeoCoordinates(new BigDecimal("-6.7714"), new BigDecimal("-79.8409"));

    @Test
    void registerCreatesAnActivePlotWithoutACrop() {
        Plot plot = Plot.register(FARM_ID, "Parcela 1", new BigDecimal("2.5"), COORDINATES);

        assertThat(plot.isActive()).isTrue();
        assertThat(plot.getCropId()).isEmpty();
        assertThat(plot.getFarmId()).isEqualTo(FARM_ID);
    }

    @Test
    void registerRejectsANonPositiveArea() {
        assertThatThrownBy(() -> Plot.register(FARM_ID, "Parcela 1", BigDecimal.ZERO, COORDINATES))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void assignCropSetsTheCropAndPublishesCropAssignedToPlotEvent() {
        Plot plot = Plot.register(FARM_ID, "Parcela 1", new BigDecimal("2.5"), COORDINATES);
        CropId cropId = new CropId(9L);

        plot.assignCrop(cropId);

        assertThat(plot.getCropId()).contains(cropId);
        var events = plot.pullDomainEvents();
        assertThat(events).hasSize(1);
        assertThat(events.get(0)).isInstanceOfSatisfying(
                CropAssignedToPlotEvent.class, event -> assertThat(event.cropId()).isEqualTo(cropId));
    }

    @Test
    void deactivateMakesThePlotInactive() {
        Plot plot = Plot.register(FARM_ID, "Parcela 1", new BigDecimal("2.5"), COORDINATES);

        plot.deactivate();

        assertThat(plot.isActive()).isFalse();
    }
}
