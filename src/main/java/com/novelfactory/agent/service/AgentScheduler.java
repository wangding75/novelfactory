package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;

public interface AgentScheduler {

  AgentExecutionResult run(AgentExecutionRequest request);
}
