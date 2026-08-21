package com.tafh.finament_app.authentication.presentation.rest.refresh;

import com.tafh.finament_app.authentication.application.exception.InvalidRefreshTokenException;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenResult;
import com.tafh.finament_app.authentication.application.usecase.refresh.RefreshTokenUseCase;
import com.tafh.finament_app.authentication.presentation.cookie.RefreshTokenCookieFactory;
import com.tafh.finament_app.authentication.presentation.cookie.RefreshTokenCookieReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RefreshTokenController.class)
class RefreshTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @MockitoBean
    private RefreshTokenCookieReader refreshTokenCookieReader;

    @MockitoBean
    private RefreshTokenCookieFactory refreshTokenCookieFactory;

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {

        String oldRefreshToken = "old-refresh-token";
        String newRefreshToken = "new-refresh-token";
        String accessToken = "access-token";

        Duration accessTokenLifetime = Duration.ofMinutes(15);
        Duration refreshTokenLifetime = Duration.ofDays(30);

        RefreshTokenResult result = new RefreshTokenResult(
                accessToken,
                newRefreshToken,
                accessTokenLifetime,
                refreshTokenLifetime
        );

        ResponseCookie cookie = ResponseCookie.from(
                        "refresh_token",
                        newRefreshToken
                )
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth")
                .sameSite("Strict")
                .maxAge(refreshTokenLifetime)
                .build();

        when(refreshTokenCookieReader.read(any()))
                .thenReturn(Optional.of(oldRefreshToken));

        when(refreshTokenUseCase.execute(any()))
                .thenReturn(result);

        when(refreshTokenCookieFactory.create(
                newRefreshToken,
                refreshTokenLifetime
        )).thenReturn(cookie);

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value(accessToken))
                .andExpect(jsonPath("$.expiresIn")
                        .value(900))
                .andExpect(header().string(
                        "Set-Cookie",
                        org.hamcrest.Matchers.containsString(
                                "refresh_token=" + newRefreshToken
                        )
                ));

        verify(refreshTokenCookieReader)
                .read(any());

        verify(refreshTokenUseCase)
                .execute(any());

        verify(refreshTokenCookieFactory)
                .create(
                        newRefreshToken,
                        refreshTokenLifetime
                );
    }

    @Test
    void shouldReturnUnauthorizedWhenRefreshTokenCookieIsMissing()
            throws Exception {

        when(refreshTokenCookieReader.read(any()))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                )
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(refreshTokenUseCase);
        verifyNoInteractions(refreshTokenCookieFactory);
    }

    @Test
    void shouldReturnUnauthorizedWhenRefreshTokenIsInvalid()
            throws Exception {

        when(refreshTokenCookieReader.read(any()))
                .thenReturn(Optional.of("invalid-token"));

        when(refreshTokenUseCase.execute(any()))
                .thenThrow(new InvalidRefreshTokenException());

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code")
                        .value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid refresh token"));
    }

}