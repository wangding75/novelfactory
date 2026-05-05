package com.novelfactory.plan.model;

public record BookPlanCardResponse(
    Long id,
    Long bookId,
    Long pipelineTaskId,
    String title,
    String genre,
    String platform,
    String oneLineHook,
    String coreSetting,
    String targetReaders,
    String coreConflictSummary,
    String creativeDirectionSummary,
    String rawContent) {}
