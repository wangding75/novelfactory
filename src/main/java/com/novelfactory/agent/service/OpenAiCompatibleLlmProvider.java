package com.novelfactory.agent.service;

import com.novelfactory.agent.model.LlmGenerationRequest;
import com.novelfactory.agent.model.LlmGenerationResult;
import org.springframework.stereotype.Service;

@Service
public class OpenAiCompatibleLlmProvider implements LlmProvider {

  @Override
  public String providerName() {
    return "openai-compatible";
  }

  @Override
  public LlmGenerationResult generate(LlmGenerationRequest request) {
    throw new UnsupportedOperationException("not implemented");
  }
}
