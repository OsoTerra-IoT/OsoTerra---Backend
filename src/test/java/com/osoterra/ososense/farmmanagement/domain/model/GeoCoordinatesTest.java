package com.osoterra.ososense.farmmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeoCoordinatesTest {

    @Test
    void acceptsCoordinatesWithinRange() {
        GeoCoordinates coordinates = new GeoCoordinates(new BigDecimal("-6.7714"), new BigDecimal("-79.8409"));

        assertThat(coordinates.latitude()).isEqualByComparingTo("-6.7714");
        assertThat(coordinates.longitude()).isEqualByComparingTo("-79.8409");
    }

    @Test
    void rejectsALatitudeBelowMinusNinety() {
        assertThatThrownBy(() -> new GeoCoordinates(new BigDecimal("-90.01"), BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsALatitudeAboveNinety() {
        assertThatThrownBy(() -> new GeoCoordinates(new BigDecimal("90.01"), BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsALongitudeBelowMinusOneEighty() {
        assertThatThrownBy(() -> new GeoCoordinates(BigDecimal.ZERO, new BigDecimal("-180.01")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsALongitudeAboveOneEighty() {
        assertThatThrownBy(() -> new GeoCoordinates(BigDecimal.ZERO, new BigDecimal("180.01")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
