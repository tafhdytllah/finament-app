package com.tafh.finament_app.authentication.application.usecase.refresh;

public record RefreshTokenCommand(
        String refreshToken
) {

    public RefreshTokenCommand {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken must not be blank");
        }
    }
}
