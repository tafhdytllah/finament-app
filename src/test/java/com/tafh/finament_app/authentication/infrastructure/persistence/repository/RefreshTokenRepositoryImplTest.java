package com.tafh.finament_app.authentication.infrastructure.persistence.repository;

import com.tafh.finament_app.authentication.application.port.RefreshTokenRepository;
import com.tafh.finament_app.authentication.domain.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class RefreshTokenRepositoryImplTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16");

    @Autowired
    private RefreshTokenRepository repository;

    @Test
    void shouldSaveRefreshToken() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 8, 21, 10, 0);

        LocalDateTime expiresAt =
                createdAt.plusDays(30);

        String tokenHash = "a".repeat(64);

        RefreshToken refreshToken =
                new RefreshToken(
                        id,
                        userId,
                        tokenHash,
                        expiresAt,
                        createdAt
                );

        RefreshToken saved =
                repository.save(refreshToken);

        assertThat(saved.getId())
                .isEqualTo(id);

        assertThat(saved.getUserId())
                .isEqualTo(userId);

        assertThat(saved.getTokenHash())
                .isEqualTo(tokenHash);

        assertThat(saved.getExpiresAt())
                .isEqualTo(expiresAt);

        assertThat(saved.getCreatedAt())
                .isEqualTo(createdAt);

        assertThat(saved.getRevokedAt())
                .isNull();
    }

    @Test
    void shouldFindRefreshTokenByTokenHash() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 8, 21, 10, 0);

        LocalDateTime expiresAt =
                createdAt.plusDays(30);

        String tokenHash = "b".repeat(64);

        RefreshToken refreshToken =
                new RefreshToken(
                        id,
                        userId,
                        tokenHash,
                        expiresAt,
                        createdAt
                );

        repository.save(refreshToken);

        Optional<RefreshToken> result =
                repository.findByTokenHash(tokenHash);

        assertThat(result)
                .isPresent();

        RefreshToken found =
                result.orElseThrow();

        assertThat(found.getId())
                .isEqualTo(id);

        assertThat(found.getUserId())
                .isEqualTo(userId);

        assertThat(found.getTokenHash())
                .isEqualTo(tokenHash);
    }

    @Test
    void shouldReturnEmptyWhenTokenHashDoesNotExist() {

        String tokenHash = "c".repeat(64);

        Optional<RefreshToken> result =
                repository.findByTokenHash(tokenHash);

        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldPersistAndRestoreRevokedToken() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 8, 21, 10, 0);

        LocalDateTime expiresAt =
                createdAt.plusDays(30);

        LocalDateTime revokedAt =
                createdAt.plusHours(2);

        String tokenHash = "d".repeat(64);

        RefreshToken refreshToken =
                new RefreshToken(
                        id,
                        userId,
                        tokenHash,
                        expiresAt,
                        createdAt
                );

        refreshToken.revoke(revokedAt);

        repository.save(refreshToken);

        RefreshToken found =
                repository.findByTokenHash(tokenHash)
                        .orElseThrow();

        assertThat(found.isRevoked())
                .isTrue();

        assertThat(found.getRevokedAt())
                .isEqualTo(revokedAt);
    }
}