package com.novelfactory.agent.model;

public record LlmGenerationResult(
    boolean success,
    String providerName,
    String modelName,
    String rawText,
    String structuredPayload,
    String failureReason) {}
