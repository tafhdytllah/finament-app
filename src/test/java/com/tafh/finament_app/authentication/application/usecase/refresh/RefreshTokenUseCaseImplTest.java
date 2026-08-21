package com.tafh.finament_app.authentication.application.usecase.refresh;

import com.tafh.finament_app.authentication.application.exception.InvalidRefreshTokenException;
import com.tafh.finament_app.authentication.application.port.*;
import com.tafh.finament_app.authentication.domain.entity.RefreshToken;
import com.tafh.finament_app.authentication.application.port.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenHasher tokenHasher;

    @Mock
    private RefreshTokenGenerator refreshTokenGenerator;

    @Mock
    private RefreshTokenPolicy refreshTokenPolicy;

    @Mock
    private AccessTokenGenerator accessTokenGenerator;

    @Mock
    private AccessTokenPolicy accessTokenPolicy;

    @Mock
    private IdGenerator idGenerator;

    private final Clock clock = Clock.fixed(
            Instant.parse("2026-08-17T00:00:00Z"),
            ZoneOffset.UTC
    );

    private RefreshTokenUseCaseImpl useCase;

    private static final UUID OLD_TOKEN_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID NEW_TOKEN_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID USER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 8, 17, 0, 0);

    private static final String RAW_REFRESH_TOKEN = "old-refresh-token";

    private static final String OLD_TOKEN_HASH = "old-token-hash";

    private static final String NEW_REFRESH_TOKEN = "new-refresh-token";

    private static final String NEW_TOKEN_HASH = "new-token-hash";

    private static final String ACCESS_TOKEN = "access-token";

    private static final Duration REFRESH_TOKEN_LIFETIME = Duration.ofDays(30);

    private static final Duration ACCESS_TOKEN_LIFETIME = Duration.ofMinutes(15);

    @BeforeEach
    void setUp() {
        useCase = new RefreshTokenUseCaseImpl(
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

    @Test
    void shouldRotateRefreshTokenSuccessfully() {

        RefreshToken currentToken = createActiveRefreshToken();

        when(tokenHasher.hash(RAW_REFRESH_TOKEN)).thenReturn(OLD_TOKEN_HASH);

        when(refreshTokenRepository.findByTokenHash(OLD_TOKEN_HASH)).thenReturn(Optional.of(currentToken));

        when(refreshTokenGenerator.generate()).thenReturn(NEW_REFRESH_TOKEN);

        when(tokenHasher.hash(NEW_REFRESH_TOKEN)).thenReturn(NEW_TOKEN_HASH);

        when(idGenerator.generate()).thenReturn(NEW_TOKEN_ID);

        when(refreshTokenPolicy.lifetime()).thenReturn(REFRESH_TOKEN_LIFETIME);

        when(accessTokenPolicy.lifetime()).thenReturn(ACCESS_TOKEN_LIFETIME);

        when(accessTokenGenerator.generate(USER_ID)).thenReturn(ACCESS_TOKEN);

        RefreshTokenCommand command = new RefreshTokenCommand(RAW_REFRESH_TOKEN);

        RefreshTokenResult result = useCase.execute(command);

        assertThat(result.accessToken()).isEqualTo(ACCESS_TOKEN);

        assertThat(result.refreshToken()).isEqualTo(NEW_REFRESH_TOKEN);

        assertThat(result.accessTokenLifetime()).isEqualTo(ACCESS_TOKEN_LIFETIME);

        assertThat(result.refreshTokenLifetime()).isEqualTo(REFRESH_TOKEN_LIFETIME);

        assertThat(currentToken.isRevoked()).isTrue();

        assertThat(currentToken.getRevokedAt()).isEqualTo(NOW);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository, times(2)).save(captor.capture());

        RefreshToken savedOldToken = captor.getAllValues().get(0);
        RefreshToken savedNewToken = captor.getAllValues().get(1);

        assertThat(savedOldToken).isSameAs(currentToken);

        assertThat(savedNewToken.getId()).isEqualTo(NEW_TOKEN_ID);

        assertThat(savedNewToken.getUserId()).isEqualTo(USER_ID);

        assertThat(savedNewToken.getTokenHash()).isEqualTo(NEW_TOKEN_HASH);

        assertThat(savedNewToken.getCreatedAt()).isEqualTo(NOW);

        assertThat(savedNewToken.getExpiresAt()).isEqualTo(NOW.plus(REFRESH_TOKEN_LIFETIME));

        assertThat(savedNewToken.getRevokedAt()).isNull();

        verify(tokenHasher).hash(RAW_REFRESH_TOKEN);

        verify(tokenHasher).hash(NEW_REFRESH_TOKEN);

        verify(accessTokenGenerator).generate(USER_ID);

        verify(accessTokenPolicy).lifetime();

    }

    @Test
    void shouldRejectWhenRefreshTokenNotFound() {

        when(tokenHasher.hash(RAW_REFRESH_TOKEN)).thenReturn(OLD_TOKEN_HASH);

        when(refreshTokenRepository.findByTokenHash(OLD_TOKEN_HASH)).thenReturn(Optional.empty());

        RefreshTokenCommand command = new RefreshTokenCommand(RAW_REFRESH_TOKEN);

        assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(InvalidRefreshTokenException.class);

        verify(refreshTokenRepository).findByTokenHash(OLD_TOKEN_HASH);

        verifyNoInteractions(
                refreshTokenGenerator,
                accessTokenGenerator,
                idGenerator,
                refreshTokenPolicy
        );

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void shouldRejectWhenRefreshTokenIsExpired() {

        RefreshToken expiredToken = new RefreshToken(
                OLD_TOKEN_ID,
                USER_ID,
                OLD_TOKEN_HASH,
                NOW.minusSeconds(1),
                NOW.minusDays(30)
        );

        when(tokenHasher.hash(RAW_REFRESH_TOKEN)).thenReturn(OLD_TOKEN_HASH);

        when(refreshTokenRepository.findByTokenHash(OLD_TOKEN_HASH)).thenReturn(Optional.of(expiredToken));

        RefreshTokenCommand command = new RefreshTokenCommand(RAW_REFRESH_TOKEN);

        assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(InvalidRefreshTokenException.class);

        verify(refreshTokenRepository).findByTokenHash(OLD_TOKEN_HASH);

        verifyNoInteractions(
                refreshTokenGenerator,
                accessTokenGenerator,
                idGenerator,
                refreshTokenPolicy
        );

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void shouldRejectWhenRefreshTokenIsRevoked() {

        RefreshToken revokedToken = createActiveRefreshToken();

        revokedToken.revoke(
                NOW.minusHours(1)
        );

        when(tokenHasher.hash(RAW_REFRESH_TOKEN)).thenReturn(OLD_TOKEN_HASH);

        when(refreshTokenRepository.findByTokenHash(OLD_TOKEN_HASH)).thenReturn(Optional.of(revokedToken));

        RefreshTokenCommand command = new RefreshTokenCommand(RAW_REFRESH_TOKEN);

        assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(InvalidRefreshTokenException.class);

        verify(refreshTokenRepository).findByTokenHash(OLD_TOKEN_HASH);

        verifyNoInteractions(
                refreshTokenGenerator,
                accessTokenGenerator,
                idGenerator,
                refreshTokenPolicy
        );

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void shouldPropagateRepositoryFailure() {

        RuntimeException repositoryException = new RuntimeException("Database unavailable");

        when(tokenHasher.hash(RAW_REFRESH_TOKEN)).thenReturn(OLD_TOKEN_HASH);

        when(refreshTokenRepository.findByTokenHash(OLD_TOKEN_HASH)).thenThrow(repositoryException);

        RefreshTokenCommand command = new RefreshTokenCommand(RAW_REFRESH_TOKEN);

        assertThatThrownBy(() -> useCase.execute(command)).isSameAs(repositoryException);
    }

    private RefreshToken createActiveRefreshToken() {
        return new RefreshToken(
                OLD_TOKEN_ID,
                USER_ID,
                OLD_TOKEN_HASH,
                NOW.plusDays(10),
                NOW.minusDays(1)
        );
    }
}