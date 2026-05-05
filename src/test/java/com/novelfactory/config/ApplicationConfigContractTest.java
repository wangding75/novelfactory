package com.novelfactory.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ApplicationConfigContractTest {

  @Test
  void applicationYaml_containsDatasourceAndActuatorConfiguration() throws Exception {
    String content = Files.readString(Path.of("src/main/resources/application.yml"));

    assertThat(content).contains("spring:");
    assertThat(content).contains("datasource:");
    assertThat(content).contains("management:");
    assertThat(content).contains("springdoc:");
  }

  @Test
  void dockerfile_existsAndExposesServicePort() throws Exception {
    String content = Files.readString(Path.of("Dockerfile"));

    assertThat(content).contains("FROM eclipse-temurin:21-jre");
    assertThat(content).contains("EXPOSE 8080");
    assertThat(content).contains("ENTRYPOINT");
  }
}
