package com.tafh.finament_app.authentication.infrastructure.persistence.mapper;

import com.tafh.finament_app.authentication.domain.entity.RefreshToken;
import com.tafh.finament_app.authentication.infrastructure.persistence.entity.RefreshTokenJpaEntity;

public class RefreshTokenMapper {

    private RefreshTokenMapper() {

    }

    public static RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        RefreshToken refreshToken = new RefreshToken(
                entity.getId(),
                entity.getUserId(),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );

        if (entity.getRevokedAt() != null) {
            refreshToken.revoke(entity.getRevokedAt());
        }

        return refreshToken;
    }

    public static RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        return new RefreshTokenJpaEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getTokenHash(),
                domain.getExpiresAt(),
                domain.getRevokedAt(),
                domain.getCreatedAt(),
                null
        );
    }
}
