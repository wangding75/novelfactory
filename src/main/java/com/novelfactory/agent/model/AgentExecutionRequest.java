package com.novelfactory.agent.model;

import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;

public record AgentExecutionRequest(
    Long pipelineTaskId,
    Long bookId,
    PipelineTaskType taskType,
    GenerationTarget generationTarget,
    TriggerSource triggerSource) {}
