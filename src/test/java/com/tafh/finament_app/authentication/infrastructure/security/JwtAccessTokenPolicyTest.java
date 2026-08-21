package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAccessTokenPolicyTest {

    @Test
    void shouldReturnAccessTokenLifetimeFromProperties() {

        Duration accessExpiration = Duration.ofMinutes(15);

        JwtProperties properties = new JwtProperties(
                "test-secret",
                "finament",
                accessExpiration,
                Duration.ofDays(30)
        );

        JwtAccessTokenPolicy policy = new JwtAccessTokenPolicy(properties);

        assertThat(policy.lifetime())
                .isEqualTo(accessExpiration);
    }

}