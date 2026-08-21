package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JwtAccessTokenGeneratorTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private static final String ISSUER = "finament-app";

    private static final UUID USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    private JwtAccessTokenGenerator generator;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(
                SECRET,
                ISSUER,
                Duration.ofHours(1),
                Duration.ofDays(30)
        );

        Clock clock = Clock.fixed(
                NOW,
                ZoneOffset.UTC
        );

        generator = new JwtAccessTokenGenerator(
                properties,
                clock
        );

        signingKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void shouldGenerateValidAccessToken() {
        String token = generator.generate(USER_ID);

        assertThat(token)
                .isNotBlank();
    }

    @Test
    void shouldGenerateTokenWithExpectedClaims() {
        String token = generator.generate(USER_ID);

        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .clock(() -> Date.from(NOW))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(USER_ID.toString());

        assertThat(claims.getIssuer()).isEqualTo(ISSUER);

        assertThat(claims.getIssuedAt()).isEqualTo(Date.from(NOW));

        assertThat(claims.getExpiration()).isEqualTo(
                Date.from(NOW.plus(Duration.ofHours(1)))
        );
    }

    @Test
    void shouldGenerateDifferentTokenForDifferentUser() {
        UUID anotherUserId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");

        String firstToken = generator.generate(USER_ID);
        String secondToken = generator.generate(anotherUserId);

        assertThat(firstToken).isNotEqualTo(secondToken);
    }

}