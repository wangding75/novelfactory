package com.novelfactory.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI novelFactoryOpenApi() {
    return new OpenAPI().info(new Info().title("Novel Factory API").version("v1"));
  }
}
