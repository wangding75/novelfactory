package com.novelfactory.agent.repository;

import com.novelfactory.agent.model.AgentExecutionLogEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisAgentExecutionLogRepository implements AgentExecutionLogRepository {

  @Override
  public AgentExecutionLogEntity save(AgentExecutionLogEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }
}
