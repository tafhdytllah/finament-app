package com.tafh.finament_app.authentication.infrastructure.persistence.repository;

import com.tafh.finament_app.authentication.application.port.RefreshTokenRepository;
import com.tafh.finament_app.authentication.domain.entity.RefreshToken;
import com.tafh.finament_app.authentication.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.tafh.finament_app.authentication.infrastructure.persistence.mapper.RefreshTokenMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final SpringDataRefreshTokenRepository repository;

    public RefreshTokenRepositoryImpl(SpringDataRefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {

        RefreshTokenJpaEntity entity =
                RefreshTokenMapper.toEntity(refreshToken);

        RefreshTokenJpaEntity saved =
                repository.save(entity);

        return RefreshTokenMapper.toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(
            String tokenHash
    ) {
        return repository
                .findByTokenHash(tokenHash)
                .map(RefreshTokenMapper::toDomain);
    }

}
