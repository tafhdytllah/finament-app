package com.tafh.finament_app.authentication.application.port;

import java.time.Duration;

public interface RefreshTokenPolicy {

    Duration lifetime();
}
