package com.novelfactory.agent.service;

import com.novelfactory.agent.model.LlmGenerationRequest;
import com.novelfactory.agent.model.LlmGenerationResult;

public interface LlmProvider {

  String providerName();

  LlmGenerationResult generate(LlmGenerationRequest request);
}
