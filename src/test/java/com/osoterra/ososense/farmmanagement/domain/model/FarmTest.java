package com.osoterra.ososense.farmmanagement.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FarmTest {

    @Test
    void registerCreatesAFarmOwnedByTheGivenUser() {
        Farm farm = Farm.register(42L, "La Esperanza", "Lambayeque", "Chiclayo", "Lambayeque");

        assertThat(farm.getOwnerId()).isEqualTo(42L);
        assertThat(farm.getName()).isEqualTo("La Esperanza");
        assertThat(farm.getDepartment()).isEqualTo("Lambayeque");
    }

    @Test
    void registerRejectsABlankName() {
        assertThatThrownBy(() -> Farm.register(42L, "  ", "Lambayeque", "Chiclayo", "Lambayeque"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
