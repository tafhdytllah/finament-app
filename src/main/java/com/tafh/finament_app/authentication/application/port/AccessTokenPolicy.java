package com.tafh.finament_app.authentication.application.port;

import java.time.Duration;

public interface AccessTokenPolicy {

    Duration lifetime();
}
