package com.novelfactory.agent.config;

public class LlmProperties {
  private String defaultProvider;
  private String defaultModel;
  private String fallbackProvider;
  private String fallbackModel;

  public String getDefaultProvider() {
    return defaultProvider;
  }

  public void setDefaultProvider(String defaultProvider) {
    this.defaultProvider = defaultProvider;
  }

  public String getDefaultModel() {
    return defaultModel;
  }

  public void setDefaultModel(String defaultModel) {
    this.defaultModel = defaultModel;
  }

  public String getFallbackProvider() {
    return fallbackProvider;
  }

  public void setFallbackProvider(String fallbackProvider) {
    this.fallbackProvider = fallbackProvider;
  }

  public String getFallbackModel() {
    return fallbackModel;
  }

  public void setFallbackModel(String fallbackModel) {
    this.fallbackModel = fallbackModel;
  }
}
