package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class JwtRefreshTokenPolicyTest {

    @Test
    void shouldReturnRefreshTokenLifetimeFromProperties() {

        Duration refreshExpiration = Duration.ofDays(30);

        JwtProperties properties = new JwtProperties(
                "test-secret",
                "finament",
                Duration.ofMinutes(15),
                refreshExpiration
        );

        JwtRefreshTokenPolicy policy = new JwtRefreshTokenPolicy(properties);

        assertThat(policy.lifetime())
                .isEqualTo(refreshExpiration);
    }

}