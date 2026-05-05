package com.novelfactory.agent.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelfactory.agent.model.LlmGenerationRequest;
import com.novelfactory.agent.model.LlmGenerationResult;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LlmGatewayContractTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void openAiCompatibleFixtures_canBeParsed() throws Exception {
    var request =
        objectMapper.readValue(
            Files.readString(Path.of("src/test/resources/contracts/llm/openai-compatible-request.json")),
            LlmGenerationRequest.class);
    var response =
        objectMapper.readValue(
            Files.readString(
                Path.of("src/test/resources/contracts/llm/openai-compatible-response.json")),
            LlmGenerationResult.class);

    assertThat(request.providerName()).isEqualTo("openai-compatible");
    assertThat(response.providerName()).isEqualTo("openai-compatible");
  }

  @Test
  void gateway_returnsStructuredPayloadForCompatibleProvider() {
    var gateway = new DefaultLlmGateway(new OpenAiCompatibleLlmProvider());

    var result =
        gateway.generate(
            new LlmGenerationRequest(
                "openai-compatible",
                "gpt-4.1-mini",
                "creative.plan-card",
                java.util.Map.of("title", "测试书名"),
                "BookPlanCard"));

    assertThat(result.success()).isTrue();
    assertThat(result.structuredPayload()).contains("oneLineHook");
  }
}
