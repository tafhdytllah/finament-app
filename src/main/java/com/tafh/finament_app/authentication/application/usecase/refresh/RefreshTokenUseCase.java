package com.tafh.finament_app.authentication.application.usecase.refresh;

public interface RefreshTokenUseCase {

    RefreshTokenResult execute(RefreshTokenCommand command);
}
