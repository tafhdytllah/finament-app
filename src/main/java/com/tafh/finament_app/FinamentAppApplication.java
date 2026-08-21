package com.tafh.finament_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FinamentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinamentAppApplication.class, args);
	}

}
