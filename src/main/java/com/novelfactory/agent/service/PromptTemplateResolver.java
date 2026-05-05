package com.novelfactory.agent.service;

import java.util.Map;

public interface PromptTemplateResolver {

  String resolve(String templateKey, Map<String, Object> variables);
}
