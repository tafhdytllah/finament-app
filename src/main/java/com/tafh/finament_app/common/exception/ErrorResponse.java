package com.tafh.finament_app.common.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
