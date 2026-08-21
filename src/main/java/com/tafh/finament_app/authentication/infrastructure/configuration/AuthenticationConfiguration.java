package com.tafh.finament_app.authentication.infrastructure.configuration;

import com.tafh.finament_app.authentication.application.port.*;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenUseCase;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AuthenticationConfiguration {

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            TokenHasher tokenHasher,
            RefreshTokenGenerator refreshTokenGenerator,
            RefreshTokenPolicy refreshTokenPolicy,
            AccessTokenGenerator accessTokenGenerator,
            AccessTokenPolicy accessTokenPolicy,
            IdGenerator idGenerator,
            Clock clock
    ) {
        return new RefreshTokenUseCaseImpl(
                refreshTokenRepository,
                tokenHasher,
                refreshTokenGenerator,
                refreshTokenPolicy,
                accessTokenGenerator,
                accessTokenPolicy,
                idGenerator,
                clock
        );
    }

}
