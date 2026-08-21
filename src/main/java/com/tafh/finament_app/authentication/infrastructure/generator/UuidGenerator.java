package com.tafh.finament_app.authentication.infrastructure.generator;

import com.tafh.finament_app.authentication.application.port.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGenerator implements IdGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
