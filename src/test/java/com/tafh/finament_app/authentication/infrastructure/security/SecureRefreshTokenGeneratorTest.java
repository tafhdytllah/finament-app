package com.tafh.finament_app.authentication.infrastructure.security;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class SecureRefreshTokenGeneratorTest {

    private final SecureRefreshTokenGenerator generator = new SecureRefreshTokenGenerator();

    @Test
    void shouldGenerateRefreshToken() {
        String token = generator.generate();

        assertThat(token).isNotBlank();
    }

    @Test
    void shouldGenerateDifferentTokens() {
        String firstToken = generator.generate();
        String secondToken = generator.generate();

        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    @Test
    void shouldGenerateUrlSafeBase64Token() {
        String token = generator.generate();

        byte[] decoded = Base64.getUrlDecoder().decode(token);

        assertThat(decoded).hasSize(32);
    }
}