package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;

public interface AgentOrchestrator {

  AgentExecutionResult execute(AgentExecutionRequest request);
}
