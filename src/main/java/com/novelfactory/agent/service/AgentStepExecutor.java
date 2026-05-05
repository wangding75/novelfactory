package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentStepContext;
import com.novelfactory.agent.model.AgentStepResult;
import com.novelfactory.agent.model.AgentStepType;

public interface AgentStepExecutor {

  AgentStepType supports();

  AgentStepResult execute(AgentStepContext context);
}
