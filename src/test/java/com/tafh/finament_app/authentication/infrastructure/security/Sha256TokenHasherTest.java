package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.application.port.TokenHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class Sha256TokenHasherTest {

    private final TokenHasher tokenHasher = new Sha256TokenHasher();

    @Test
    void shouldGenerateConsistentHashForSameToken() {
        String token = "refresh-token";

        String firstHash = tokenHasher.hash(token);
        String secondHash = tokenHasher.hash(token);

        assertThat(firstHash).isEqualTo(secondHash);
    }

    @Test
    void shouldGenerateDifferentHashForDifferentTokens() {
        String firstHash = tokenHasher.hash("refresh-token-1");

        String secondHash = tokenHasher.hash("refresh-token-2");

        assertThat(firstHash).isNotEqualTo(secondHash);
    }

    @Test
    void shouldGenerateSha256HashInHexFormat() {
        String hash = tokenHasher.hash("refresh-token");

        assertThat(hash)
                .hasSize(64)
                .matches("[0-9a-f]{64}");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r", " \t\n "})
    void shouldRejectBlankRawToken(String rawToken) {

        assertThatThrownBy(() -> tokenHasher.hash(rawToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("rawToken must not be blank");
    }

}