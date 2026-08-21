package com.tafh.finament_app.authentication.presentation.cookie;

import com.tafh.finament_app.authentication.infrastructure.configuration.RefreshTokenCookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenCookieReader {

    private final RefreshTokenCookieProperties properties;

    public RefreshTokenCookieReader(
            RefreshTokenCookieProperties properties
    ) {
        this.properties = properties;
    }

    public Optional<String> read(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (properties.name().equals(cookie.getName())) {
                return Optional.ofNullable(cookie.getValue());
            }
        }

        return Optional.empty();
    }
}