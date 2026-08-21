package com.tafh.finament_app.authentication.presentation.cookie;

import com.tafh.finament_app.authentication.infrastructure.configuration.RefreshTokenCookieProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieFactory {

    private final RefreshTokenCookieProperties properties;

    public RefreshTokenCookieFactory(RefreshTokenCookieProperties properties) {
        this.properties = properties;
    }

    public ResponseCookie create(
            String refreshToken,
            Duration maxAge
    ) {
        return ResponseCookie.from(
                        properties.name(),
                        refreshToken
                )
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .path(properties.path())
                .sameSite(properties.sameSite())
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie clear() {
        return ResponseCookie.from(
                        properties.name(),
                        ""
                )
                .httpOnly(properties.httpOnly())
                .secure(properties.secure())
                .path(properties.path())
                .sameSite(properties.sameSite())
                .maxAge(Duration.ZERO)
                .build();
    }
}
