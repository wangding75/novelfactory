package com.novelfactory.pipeline.model;

public record PipelineTaskResponse(
    Long taskId,
    Long bookId,
    PipelineTaskType taskType,
    PipelineTaskStatus status,
    String resultMessage) {}
