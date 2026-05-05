package com.novelfactory.agent.service;

import com.novelfactory.agent.model.LlmGenerationRequest;
import com.novelfactory.agent.model.LlmGenerationResult;
import org.springframework.stereotype.Service;

@Service
public class DefaultLlmGateway implements LlmGateway {
  private final LlmProvider llmProvider;

  public DefaultLlmGateway(LlmProvider llmProvider) {
    this.llmProvider = llmProvider;
  }

  @Override
  public LlmGenerationResult generate(LlmGenerationRequest request) {
    return llmProvider.generate(request);
  }
}
