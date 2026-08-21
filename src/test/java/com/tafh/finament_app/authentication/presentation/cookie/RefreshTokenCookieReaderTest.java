package com.tafh.finament_app.authentication.presentation.cookie;

import com.tafh.finament_app.authentication.infrastructure.configuration.RefreshTokenCookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefreshTokenCookieReaderTest {

    private final RefreshTokenCookieProperties properties =
            new RefreshTokenCookieProperties(
                    "refresh_token",
                    "/api/v1/auth",
                    true,
                    true,
                    "Strict"
            );

    private final RefreshTokenCookieReader reader =
            new RefreshTokenCookieReader(properties);

    @Test
    void shouldReturnRefreshTokenWhenCookieExists() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        Cookie refreshTokenCookie =
                new Cookie("refresh_token", "abc123");

        when(request.getCookies())
                .thenReturn(new Cookie[]{
                        refreshTokenCookie
                });

        Optional<String> result =
                reader.read(request);

        assertThat(result)
                .contains("abc123");
    }

    @Test
    void shouldReturnEmptyWhenCookiesAreAbsent() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getCookies())
                .thenReturn(null);

        Optional<String> result =
                reader.read(request);

        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenRefreshTokenCookieDoesNotExist() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        Cookie otherCookie =
                new Cookie("session", "abc123");

        when(request.getCookies())
                .thenReturn(new Cookie[]{
                        otherCookie
                });

        Optional<String> result =
                reader.read(request);

        assertThat(result)
                .isEmpty();
    }

}