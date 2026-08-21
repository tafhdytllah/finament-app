package com.tafh.finament_app.authentication.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.refresh-token-cookie")
public record RefreshTokenCookieProperties(
        String name,
        String path,
        boolean secure,
        boolean httpOnly,
        String sameSite
) {
}
