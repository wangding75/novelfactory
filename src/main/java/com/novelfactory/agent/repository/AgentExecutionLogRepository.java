package com.novelfactory.agent.repository;

import com.novelfactory.agent.model.AgentExecutionLogEntity;

public interface AgentExecutionLogRepository {

  AgentExecutionLogEntity save(AgentExecutionLogEntity entity);
}
