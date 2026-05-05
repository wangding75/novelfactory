package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentStepType;
import com.novelfactory.agent.model.LlmGenerationRequest;

public interface ModelRoutingPolicy {

  LlmGenerationRequest route(AgentStepType stepType, LlmGenerationRequest request);
}
