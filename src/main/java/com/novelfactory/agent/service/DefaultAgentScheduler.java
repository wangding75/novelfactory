package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;
import org.springframework.stereotype.Service;

@Service
public class DefaultAgentScheduler implements AgentScheduler {

  @Override
  public AgentExecutionResult run(AgentExecutionRequest request) {
    throw new UnsupportedOperationException("not implemented");
  }
}
