package com.tafh.finament_app.authentication.domain.repository;

import com.tafh.finament_app.authentication.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
