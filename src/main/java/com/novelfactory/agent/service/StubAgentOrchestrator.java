package com.novelfactory.agent.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.pipeline.model.PipelineTaskArtifactRefs;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import com.novelfactory.pipeline.model.PipelineTaskStatus;

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
    if (request.taskType() == null) {
      throw new BusinessException(ErrorCode.VALIDATION_ERROR, "taskType must not be null");
    }
    return new AgentExecutionResult(
        true,
        PipelineTaskStatus.COMPLETED,
        PipelineTaskStage.COMPLETED,
        STUB_SUCCESS_MESSAGE,
        null,
        PipelineTaskArtifactRefs.empty());
  }
}
