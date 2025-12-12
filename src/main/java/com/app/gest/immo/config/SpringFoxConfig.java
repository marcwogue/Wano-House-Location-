package com.app.gest.immo.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringFoxConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI();
	}

}
