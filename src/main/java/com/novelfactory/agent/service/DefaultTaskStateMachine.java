package com.novelfactory.agent.service;

import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskEvent;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import org.springframework.stereotype.Service;

@Service
public class DefaultTaskStateMachine implements TaskStateMachine {

  @Override
  public PipelineTaskStage transit(
      PipelineTaskStage currentStage, PipelineTaskEvent event, GenerationTarget generationTarget) {
    throw new BusinessException(
        ErrorCode.INVALID_TASK_STATE_TRANSITION, "state transition not implemented");
  }
}
