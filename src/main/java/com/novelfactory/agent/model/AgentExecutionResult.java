package com.novelfactory.agent.model;

import com.novelfactory.pipeline.model.PipelineTaskArtifactRefs;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import com.novelfactory.pipeline.model.PipelineTaskStatus;

public record AgentExecutionResult(
    boolean success,
    PipelineTaskStatus status,
    PipelineTaskStage currentStage,
    String message,
    String failureReason,
    PipelineTaskArtifactRefs artifactRefs) {}
