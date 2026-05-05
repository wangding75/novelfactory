package com.novelfactory.creative.model;

public record AddictionCanvasResponse(
    Long id,
    Long bookId,
    Long pipelineTaskId,
    String coreEmotionalHook,
    String emotionalFuel,
    String readerFantasy,
    String suppressionReleaseRhythm,
    String forbiddenRules,
    String rawContent) {}
