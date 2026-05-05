package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;
import org.springframework.stereotype.Service;

@Service
public class DefaultAgentOrchestrator implements AgentOrchestrator {
  private final AgentScheduler agentScheduler;

  public DefaultAgentOrchestrator(AgentScheduler agentScheduler) {
    this.agentScheduler = agentScheduler;
  }

  @Override
  public AgentExecutionResult execute(AgentExecutionRequest request) {
    return agentScheduler.run(request);
  }
}
