package com.tafh.finament_app.authentication.presentation.rest.refresh;

public record RefreshTokenResponse(
        String accessToken,
        long expiresIn
) {
}
