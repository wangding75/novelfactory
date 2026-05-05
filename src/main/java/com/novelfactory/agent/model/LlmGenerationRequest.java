package com.novelfactory.agent.model;

import java.util.Map;

public record LlmGenerationRequest(
    String providerName,
    String modelName,
    String promptTemplateKey,
    Map<String, Object> variables,
    String responseSchemaName) {}
