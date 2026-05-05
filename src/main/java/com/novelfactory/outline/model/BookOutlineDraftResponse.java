package com.novelfactory.outline.model;

public record BookOutlineDraftResponse(
    Long id,
    Long bookId,
    Long pipelineTaskId,
    String positioning,
    String worldSummary,
    String protagonistProfile,
    String mainConflict,
    String stagePlotOverview,
    String arcStructure,
    String rawContent) {}
