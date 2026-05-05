package com.novelfactory.creative.model;

public record WorldSettingResponse(
    Long id,
    Long bookId,
    Long pipelineTaskId,
    String positioning,
    String worldSummary,
    String coreRules,
    String factionStructure,
    String forbiddenItems,
    String rawContent) {}
