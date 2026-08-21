package com.tafh.finament_app.authentication.presentation.cookie;

import com.tafh.finament_app.authentication.infrastructure.configuration.RefreshTokenCookieProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenCookieFactoryTest {
    private final RefreshTokenCookieProperties properties =
            new RefreshTokenCookieProperties(
                    "refresh_token",
                    "/api/v1/auth",
                    true,
                    true,
                    "Strict"
            );

    private final RefreshTokenCookieFactory factory =
            new RefreshTokenCookieFactory(properties);

    @Test
    void shouldCreateRefreshTokenCookie() {

        ResponseCookie cookie = factory.create(
                "refresh-token-value",
                Duration.ofDays(30)
        );

        assertThat(cookie.getName())
                .isEqualTo("refresh_token");

        assertThat(cookie.getValue())
                .isEqualTo("refresh-token-value");

        assertThat(cookie.isHttpOnly())
                .isTrue();

        assertThat(cookie.isSecure())
                .isTrue();

        assertThat(cookie.getPath())
                .isEqualTo("/api/v1/auth");

        assertThat(cookie.getSameSite())
                .isEqualTo("Strict");

        assertThat(cookie.getMaxAge())
                .isEqualTo(Duration.ofDays(30));
    }

    @Test
    void shouldCreateExpiredCookieWhenClearing() {

        ResponseCookie cookie = factory.clear();

        assertThat(cookie.getName())
                .isEqualTo("refresh_token");

        assertThat(cookie.getValue())
                .isEmpty();

        assertThat(cookie.getMaxAge())
                .isEqualTo(Duration.ZERO);
    }
}