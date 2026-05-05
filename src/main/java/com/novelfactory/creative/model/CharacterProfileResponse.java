package com.novelfactory.creative.model;

public record CharacterProfileResponse(
    Long id,
    Long bookId,
    Long pipelineTaskId,
    CharacterProfileRoleType roleType,
    String name,
    String roleLabel,
    String background,
    String motivation,
    String personalityTraits,
    String growthArc,
    String rawContent) {}
