package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.application.port.AccessTokenPolicy;
import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtAccessTokenPolicy implements AccessTokenPolicy {

    private final JwtProperties properties;

    public JwtAccessTokenPolicy(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public Duration lifetime() {

        return properties.expiration();
    }
}
