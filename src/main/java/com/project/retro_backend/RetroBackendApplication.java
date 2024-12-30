package com.project.retro_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RetroBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetroBackendApplication.class, args);
	}

}
