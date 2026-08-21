package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.application.port.RefreshTokenPolicy;
import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtRefreshTokenPolicy implements RefreshTokenPolicy {

    private final JwtProperties properties;

    public JwtRefreshTokenPolicy(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public Duration lifetime() {

        return properties.refreshExpiration();
    }
}
