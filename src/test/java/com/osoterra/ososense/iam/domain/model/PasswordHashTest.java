package com.osoterra.ososense.iam.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordHashTest {

    @Test
    void keepsTheHashAndTheAlgorithm() {
        PasswordHash hash = new PasswordHash("$2a$10$abcdefghijklmnopqrstuv", "bcrypt");

        assertThat(hash.value()).isEqualTo("$2a$10$abcdefghijklmnopqrstuv");
        assertThat(hash.algorithm()).isEqualTo("bcrypt");
    }

    @Test
    void doesNotExposeTheHashInToString() {
        PasswordHash hash = new PasswordHash("$2a$10$abcdefghijklmnopqrstuv", "bcrypt");

        assertThat(hash.toString())
                .contains("bcrypt")
                .doesNotContain("abcdefghijklmnopqrstuv");
    }

    @Test
    void rejectsABlankHash() {
        assertThatThrownBy(() -> new PasswordHash(" ", "bcrypt"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsAMissingAlgorithm() {
        assertThatThrownBy(() -> new PasswordHash("$2a$10$abcdefghijklmnopqrstuv", null))
                .isInstanceOf(NullPointerException.class);
    }
}
