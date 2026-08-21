package com.tafh.finament_app.authentication.presentation.rest.refresh;

import com.tafh.finament_app.authentication.application.exception.InvalidRefreshTokenException;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenCommand;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenResult;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenUseCase;
import com.tafh.finament_app.authentication.presentation.cookie.RefreshTokenCookieFactory;
import com.tafh.finament_app.authentication.presentation.cookie.RefreshTokenCookieReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class RefreshTokenController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RefreshTokenCookieReader refreshTokenCookieReader;
    private final RefreshTokenCookieFactory refreshTokenCookieFactory;

    public RefreshTokenController(RefreshTokenUseCase refreshTokenUseCase, RefreshTokenCookieReader refreshTokenCookieReader, RefreshTokenCookieFactory refreshTokenCookieFactory) {
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.refreshTokenCookieReader = refreshTokenCookieReader;
        this.refreshTokenCookieFactory = refreshTokenCookieFactory;
    }


    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = refreshTokenCookieReader
                .read(request)
                .orElseThrow(InvalidRefreshTokenException::new);

        RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

        RefreshTokenResult result = refreshTokenUseCase.execute(command);

        response.addHeader(
                "Set-Cookie",
                refreshTokenCookieFactory.create(
                        result.refreshToken(),
                        result.refreshTokenLifetime()
                ).toString()
        );

        RefreshTokenResponse body = new RefreshTokenResponse(
                result.accessToken(),
                result.accessTokenLifetime().toSeconds()
        );

        return ResponseEntity.ok(body);
    }

}
