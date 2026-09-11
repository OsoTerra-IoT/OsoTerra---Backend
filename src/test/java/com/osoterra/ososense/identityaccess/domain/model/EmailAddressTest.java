package com.osoterra.ososense.identityaccess.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailAddressTest {

    @Test
    void acceptsAWellFormedAddressAndNormalizesItToLowerCase() {
        EmailAddress email = new EmailAddress("Farmer@OsoTerra.com");

        assertThat(email.value()).isEqualTo("farmer@osoterra.com");
    }

    @Test
    void rejectsAnAddressWithoutAnAtSign() {
        assertThatThrownBy(() -> new EmailAddress("not-an-email"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAnAddressWithoutADomain() {
        assertThatThrownBy(() -> new EmailAddress("farmer@"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
