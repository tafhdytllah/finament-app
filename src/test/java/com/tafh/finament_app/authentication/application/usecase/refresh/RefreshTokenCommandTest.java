package com.tafh.finament_app.authentication.application.usecase.refresh;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class RefreshTokenCommandTest {

    @Test
    void shouldCreateCommandWithValidRefreshToken() {

        String refreshToken = "valid-refresh-token";

        RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

        assertThat(command.refreshToken()).isEqualTo(refreshToken);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r", " \t\n "})
    void shouldRejectBlankRefreshToken(String refreshToken) {
        assertThatThrownBy(() -> new RefreshTokenCommand(refreshToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("refreshToken must not be blank");
    }

}