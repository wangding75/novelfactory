package com.novelfactory.agent.service;

import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskEvent;
import com.novelfactory.pipeline.model.PipelineTaskStage;

public interface TaskStateMachine {

  PipelineTaskStage transit(
      PipelineTaskStage currentStage, PipelineTaskEvent event, GenerationTarget generationTarget);
}
