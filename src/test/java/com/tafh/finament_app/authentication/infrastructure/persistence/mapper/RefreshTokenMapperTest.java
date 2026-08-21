package com.tafh.finament_app.authentication.infrastructure.persistence.mapper;

import com.tafh.finament_app.authentication.domain.entity.RefreshToken;
import com.tafh.finament_app.authentication.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenMapperTest {

    @Test
    void shouldMapDomainToEntity() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String tokenHash = "a".repeat(64);
        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 21, 10, 0);
        LocalDateTime expiresAt =   createdAt.plusDays(30);

        RefreshToken domain = new RefreshToken(
                id,
                userId,
                tokenHash,
                expiresAt,
                createdAt
        );

        RefreshTokenJpaEntity entity = RefreshTokenMapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getTokenHash()).isEqualTo(tokenHash);
        assertThat(entity.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getRevokedAt()).isNull();
    }

    @Test
    void shouldMapEntityToDomain() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String tokenHash = "a".repeat(64);
        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 21, 10, 0);
        LocalDateTime expiresAt = createdAt.plusDays(30);

        RefreshTokenJpaEntity entity =
                new RefreshTokenJpaEntity(
                        id,
                        userId,
                        tokenHash,
                        expiresAt,
                        null,
                        createdAt,
                        null
                );

        RefreshToken domain = RefreshTokenMapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getUserId()).isEqualTo(userId);
        assertThat(domain.getTokenHash()).isEqualTo(tokenHash);
        assertThat(domain.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(domain.getCreatedAt()).isEqualTo(createdAt);
        assertThat(domain.getRevokedAt()).isNull();
    }

    @Test
    void shouldRestoreRevokedStateFromEntity() {

        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String tokenHash = "a".repeat(64);
        LocalDateTime createdAt = LocalDateTime.of(2026, 8, 21, 10, 0);
        LocalDateTime expiresAt = createdAt.plusDays(30);
        LocalDateTime revokedAt = LocalDateTime.of(2026, 8, 21, 12, 0);

        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity(
                id,
                userId,
                tokenHash,
                expiresAt,
                revokedAt,
                createdAt,
                null
        );

        RefreshToken domain = RefreshTokenMapper.toDomain(entity);

        assertThat(domain.getRevokedAt())
                .isEqualTo(revokedAt);

        assertThat(domain.isRevoked())
                .isTrue();
    }

}