package com.novelfactory.agent.model;

import com.novelfactory.pipeline.model.PipelineTaskArtifactRefs;
import com.novelfactory.pipeline.model.PipelineTaskEvent;

public record AgentStepResult(
    AgentStepType stepType,
    AgentStepDecision decision,
    PipelineTaskEvent nextEvent,
    String message,
    String failureReason,
    PipelineTaskArtifactRefs artifactRefs) {}
