package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class StubAgentOrchestrator implements AgentOrchestrator {

  private static final String STUB_SUCCESS_MESSAGE = "stub execution completed";

  @Override
  public AgentExecutionResult execute(AgentExecutionRequest request) {
    if (request == null) {
      throw new BusinessException(ErrorCode.VALIDATION_ERROR, "request must not be null");
    }
    if (request.bookId() == null) {
      throw new BusinessException(ErrorCode.VALIDATION_ERROR, "bookId must not be null");
    }
    if (request.taskType() == null || request.taskType().isBlank()) {
      throw new BusinessException(ErrorCode.VALIDATION_ERROR, "taskType must not be blank");
    }
    return new AgentExecutionResult(true, STUB_SUCCESS_MESSAGE);
  }
}
