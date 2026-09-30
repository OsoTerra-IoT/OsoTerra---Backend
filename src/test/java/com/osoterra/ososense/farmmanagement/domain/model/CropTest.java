package com.osoterra.ososense.farmmanagement.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CropTest {

    @Test
    void registerCreatesACatalogEntry() {
        Crop crop = Crop.register("Arroz", null, new BigDecimal("3.0"), "Moderadamente sensible", null);

        assertThat(crop.getCommonName()).isEqualTo("Arroz");
        assertThat(crop.getSalinityThresholdDsM()).isEqualByComparingTo("3.0");
        assertThat(crop.getScientificName()).isEmpty();
    }

    @Test
    void registerRejectsANonPositiveThreshold() {
        assertThatThrownBy(() -> Crop.register("Arroz", null, BigDecimal.ZERO, "Sensible", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registerRejectsABlankCommonName() {
        assertThatThrownBy(() -> Crop.register(" ", null, new BigDecimal("3.0"), "Sensible", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
