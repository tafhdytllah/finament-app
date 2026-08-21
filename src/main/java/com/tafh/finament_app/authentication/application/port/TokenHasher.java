package com.tafh.finament_app.authentication.application.port;

public interface TokenHasher {

    String hash(String rawToken);
}
