package com.tafh.finament_app.authentication.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class RefreshTokenTest {

    private static final UUID ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String TOKEN_HASH = "valid-token-hash";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 8, 16, 10, 0);
    private static final LocalDateTime EXPIRES_AT = CREATED_AT.plusDays(7);

    @Test
    void shouldCreateRefreshTokenWithValidData() {

        RefreshToken refreshToken = createRefreshToken();

        assertThat(refreshToken.getId()).isEqualTo(ID);
        assertThat(refreshToken.getUserId()).isEqualTo(USER_ID);
        assertThat(refreshToken.getTokenHash()).isEqualTo(TOKEN_HASH);
        assertThat(refreshToken.getExpiresAt()).isEqualTo(EXPIRES_AT);
        assertThat(refreshToken.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(refreshToken.getRevokedAt()).isNull();
    }

    @ParameterizedTest(name = "{0} must not be null")
    @MethodSource("nullRequiredFields")
    void shouldRejectNullRequiredField(
        String field,
        UUID id,
        UUID userId,
        String tokenHash,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
    ) {
        assertThatThrownBy(() ->
                new RefreshToken(
                        id,
                        userId,
                        tokenHash,
                        expiresAt,
                        createdAt
                )
        ).isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> nullRequiredFields() {
        return Stream.of(
                Arguments.of("id", null, USER_ID, TOKEN_HASH, EXPIRES_AT, CREATED_AT),
                Arguments.of("userId", ID, null, TOKEN_HASH, EXPIRES_AT, CREATED_AT),
                Arguments.of("tokenHash", ID, USER_ID, null, EXPIRES_AT, CREATED_AT),
                Arguments.of("expiresAt", ID, USER_ID, TOKEN_HASH, null, CREATED_AT),
                Arguments.of("createdAt", ID, USER_ID, TOKEN_HASH, EXPIRES_AT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("blankTokenHashes")
    void shouldRejectBlankTokenHash(String tokenhash) {
        assertThatThrownBy(() ->
                new RefreshToken(
                        ID,
                        USER_ID,
                        tokenhash,
                        EXPIRES_AT,
                        CREATED_AT
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<String> blankTokenHashes() {
        return Stream.of(
                "",
                " ",
                "   ",
                "\t",
                "\n",
                "\r",
                " \t\n "
        );
    }

    @ParameterizedTest
    @MethodSource("invalidExpirationTimes")
    void shouldRejectInvalidExpirationTIme(LocalDateTime expiresAt) {
        assertThatThrownBy(() ->
                new RefreshToken(
                        ID,
                        USER_ID,
                        TOKEN_HASH,
                        expiresAt,
                        CREATED_AT
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<LocalDateTime> invalidExpirationTimes() {
        return Stream.of(
                CREATED_AT,
                CREATED_AT.minusSeconds(1),
                CREATED_AT.minusDays(1)
        );
    }

    @ParameterizedTest
    @MethodSource("expirationScenarios")
    void shouldDetermineExpirationCorrectly(
            LocalDateTime now,
            boolean expectedExpired
    ) {
        RefreshToken refreshToken = createRefreshToken();

        assertThat(refreshToken.isExpired(now)).isEqualTo(expectedExpired);
    }

    private static Stream<Arguments> expirationScenarios() {
        return Stream.of(
                Arguments.of(EXPIRES_AT.minusSeconds(1), false),
                Arguments.of(EXPIRES_AT, true),
                Arguments.of(EXPIRES_AT.plusSeconds(1), true)
        );
    }

    @Test
    void shouldRejectNullNowWhenCheckingExpiration() {
        RefreshToken refreshToken = createRefreshToken();

        assertThatThrownBy(() -> refreshToken.isExpired(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotBeRevokedInitially() {
        RefreshToken refreshToken = createRefreshToken();

        assertThat(refreshToken.isRevoked()).isFalse();
    }

    @Test
    void shouldBeRevokedAfterRevoke() {
        RefreshToken refreshToken = createRefreshToken();
        LocalDateTime revokedAt = CREATED_AT.plusHours(1);

        refreshToken.revoke(revokedAt);

        assertThat(refreshToken.isRevoked()).isTrue();
        assertThat(refreshToken.getRevokedAt()).isEqualTo(revokedAt);
    }

    @Test
    void shouldNotChangeRevokedAtWhenAlreadyRevoked() {
        RefreshToken refreshToken = createRefreshToken();
        LocalDateTime firstRevokedAt = CREATED_AT.plusHours(1);
        LocalDateTime secondRevokedAt = CREATED_AT.plusHours(2);

        refreshToken.revoke(firstRevokedAt);
        refreshToken.revoke(secondRevokedAt);

        assertThat(refreshToken.getRevokedAt()).isEqualTo(firstRevokedAt);
    }

    @Test
    void shouldRejectNullRevokedAt() {
        RefreshToken refreshToken = createRefreshToken();

        assertThatThrownBy(() -> refreshToken.revoke(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("activeScenarios")
    void shouldDetermineActiveStatusCorrectly(
            String field,
            boolean revoked,
            LocalDateTime now,
            boolean expectedActive
    ) {
        RefreshToken refreshToken = createRefreshToken();

        if (revoked) {
            refreshToken.revoke(CREATED_AT.plusHours(1));
        }

        assertThat(refreshToken.isActive(now)).isEqualTo(expectedActive);
    }


    private static Stream<Arguments> activeScenarios() {
        return Stream.of(
                Arguments.of("active when not revoked and not expired",
                        false, CREATED_AT.plusDays(1), true
                ),
                Arguments.of("inactive when revoked",
                        true, CREATED_AT.plusDays(1), false
                ),
                Arguments.of("inactive when expired",
                        false, EXPIRES_AT, false
                ),
                Arguments.of("inactive when revoked and expired",
                        true, EXPIRES_AT, false
                )
        );
    }

    @Test
    void shouldRejectNullNowWhenCheckingActive() {
        RefreshToken refreshToken = createRefreshToken();

        assertThatThrownBy(() -> refreshToken.isActive(null))
                .isInstanceOf(NullPointerException.class);
    }

    private static RefreshToken createRefreshToken() {
        return new RefreshToken(
                ID,
                USER_ID,
                TOKEN_HASH,
                EXPIRES_AT,
                CREATED_AT
        );
    }

}