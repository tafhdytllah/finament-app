package com.tafh.finament_app.authentication.application.usecase.refresh;

import com.tafh.finament_app.authentication.application.exception.InvalidRefreshTokenException;
import com.tafh.finament_app.authentication.application.port.*;
import com.tafh.finament_app.authentication.domain.entity.RefreshToken;

import java.time.Clock;
import java.time.LocalDateTime;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase{

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHasher tokenHasher;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenPolicy refreshTokenPolicy;
    private final AccessTokenGenerator accessTokenGenerator;
    private final AccessTokenPolicy accessTokenPolicy;

    public RefreshTokenUseCaseImpl(RefreshTokenRepository refreshTokenRepository, TokenHasher tokenHasher, RefreshTokenGenerator refreshTokenGenerator, RefreshTokenPolicy refreshTokenPolicy, AccessTokenGenerator accessTokenGenerator, AccessTokenPolicy accessTokenPolicy, IdGenerator idGenerator, Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenHasher = tokenHasher;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenPolicy = refreshTokenPolicy;
        this.accessTokenGenerator = accessTokenGenerator;
        this.accessTokenPolicy = accessTokenPolicy;
        this.idGenerator = idGenerator;
        this.clock = clock;
    }

    private final IdGenerator idGenerator;
    private final Clock clock;



    @Override
    public RefreshTokenResult execute(RefreshTokenCommand command) {

        LocalDateTime now = LocalDateTime.now(clock);

        String tokenHash = tokenHasher.hash(command.refreshToken());

        RefreshToken currentToken = refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!currentToken.isActive(now)) {
            throw new InvalidRefreshTokenException();
        }

        currentToken.revoke(now);
        refreshTokenRepository.save(currentToken);

        String newRefreshToken = refreshTokenGenerator.generate();
        String newTokenHash = tokenHasher.hash(newRefreshToken);

        RefreshToken newToken = new RefreshToken(
                idGenerator.generate(),
                currentToken.getUserId(),
                newTokenHash,
                now.plus(refreshTokenPolicy.lifetime()),
                now
        );

        refreshTokenRepository.save(newToken);

        String accessToken = accessTokenGenerator.generate(
                currentToken.getUserId()
        );

        return new RefreshTokenResult(
                accessToken,
                newRefreshToken,
                accessTokenPolicy.lifetime(),
                refreshTokenPolicy.lifetime()
        );
    }
}
