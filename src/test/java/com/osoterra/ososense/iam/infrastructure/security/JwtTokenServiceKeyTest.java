package com.osoterra.ososense.iam.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.Test;

class JwtTokenServiceKeyTest {

    private static final String BASE64_SECRET = "vlBw3pXcBi/t/g6v65zlzSOxKSjw37Ukb3Up5vq1jfdBCZ5lzMiP3meI33nAq9nr";

    @Test
    void keepsLongBase64SecretsAsIs() {
        assertThat(JwtTokenService.keyBytes(BASE64_SECRET)).isEqualTo(Decoders.BASE64.decode(BASE64_SECRET));
    }

    @Test
    void stretchesShortOrPlainTextSecretsTo256Bits() {
        assertThat(JwtTokenService.keyBytes("osoterra")).hasSize(32);
        assertThat(JwtTokenService.keyBytes("mi clave secreta!")).hasSize(32);
        assertThat(JwtTokenService.keyBytes("osoterra")).isEqualTo(JwtTokenService.keyBytes("osoterra"));
    }
}
