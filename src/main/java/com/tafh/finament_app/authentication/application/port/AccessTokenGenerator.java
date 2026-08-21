package com.tafh.finament_app.authentication.application.port;

import java.util.UUID;

public interface AccessTokenGenerator {

    String generate(UUID userId);
}
