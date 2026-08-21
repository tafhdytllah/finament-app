package com.tafh.finament_app.authentication.application.usecase.refresh;

import java.time.Duration;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken,
        Duration accessTokenLifetime,
        Duration refreshTokenLifetime
) {
}
