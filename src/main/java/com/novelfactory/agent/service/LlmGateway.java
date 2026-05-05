package com.novelfactory.agent.service;

import com.novelfactory.agent.model.LlmGenerationRequest;
import com.novelfactory.agent.model.LlmGenerationResult;

public interface LlmGateway {

  LlmGenerationResult generate(LlmGenerationRequest request);
}
