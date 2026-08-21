package com.tafh.finament_app.authentication.infrastructure.security;

import com.tafh.finament_app.authentication.application.port.AccessTokenGenerator;
import com.tafh.finament_app.authentication.infrastructure.configuration.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtAccessTokenGenerator implements AccessTokenGenerator {

    private final JwtProperties properties;
    private final Clock clock;

    public JwtAccessTokenGenerator(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public String generate(UUID userId) {

        Instant now = Instant.now(clock);
        Instant expiration = now.plus(properties.expiration());

        return Jwts.builder()
                .subject(userId.toString())
                .issuer(properties.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();

    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                properties.secret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}
