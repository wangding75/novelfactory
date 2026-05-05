package com.novelfactory.pipeline.model;

import jakarta.validation.constraints.NotNull;

public record CreatePipelineTaskRequest(
    @NotNull Long bookId,
    @NotNull PipelineTaskType taskType,
    @NotNull TriggerSource triggerSource) {}
