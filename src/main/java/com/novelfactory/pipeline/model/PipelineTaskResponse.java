package com.novelfactory.pipeline.model;

public record PipelineTaskResponse(
    Long taskId,
    Long bookId,
    PipelineTaskType taskType,
    GenerationTarget generationTarget,
    PipelineTaskStatus status,
    PipelineTaskStage currentStage,
    String resultMessage,
    String failureReason,
    PipelineTaskArtifactRefs artifactRefs) {}
