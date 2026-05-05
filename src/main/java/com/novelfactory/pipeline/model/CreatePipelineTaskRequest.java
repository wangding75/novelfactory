package com.novelfactory.pipeline.model;

import jakarta.validation.constraints.NotNull;

public record CreatePipelineTaskRequest(
    @NotNull Long bookId,
    @NotNull PipelineTaskType taskType,
    @NotNull GenerationTarget generationTarget,
    @NotNull TriggerSource triggerSource) {}
