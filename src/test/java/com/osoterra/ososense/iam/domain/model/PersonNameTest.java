package com.osoterra.ososense.iam.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonNameTest {

    @Test
    void joinsFirstAndLastNameIntoTheFullName() {
        PersonName name = new PersonName("Diego", "Ramos");

        assertThat(name.getFullName()).isEqualTo("Diego Ramos");
    }

    @Test
    void rejectsABlankFirstName() {
        assertThatThrownBy(() -> new PersonName("  ", "Ramos"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsABlankLastName() {
        assertThatThrownBy(() -> new PersonName("Diego", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAMissingName() {
        assertThatThrownBy(() -> new PersonName(null, "Ramos"))
                .isInstanceOf(NullPointerException.class);
    }
}
