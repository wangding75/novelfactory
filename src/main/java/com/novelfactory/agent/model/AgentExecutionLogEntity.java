package com.novelfactory.agent.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("agent_execution_log")
public class AgentExecutionLogEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long pipelineTaskId;
  private Long bookId;
  private AgentStepType stepType;
  private String providerName;
  private String modelName;
  private String requestSnapshot;
  private String responseSnapshot;
  private boolean success;
  private String failureReason;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPipelineTaskId() {
    return pipelineTaskId;
  }

  public void setPipelineTaskId(Long pipelineTaskId) {
    this.pipelineTaskId = pipelineTaskId;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public AgentStepType getStepType() {
    return stepType;
  }

  public void setStepType(AgentStepType stepType) {
    this.stepType = stepType;
  }

  public String getProviderName() {
    return providerName;
  }

  public void setProviderName(String providerName) {
    this.providerName = providerName;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getRequestSnapshot() {
    return requestSnapshot;
  }

  public void setRequestSnapshot(String requestSnapshot) {
    this.requestSnapshot = requestSnapshot;
  }

  public String getResponseSnapshot() {
    return responseSnapshot;
  }

  public void setResponseSnapshot(String responseSnapshot) {
    this.responseSnapshot = responseSnapshot;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getFailureReason() {
    return failureReason;
  }

  public void setFailureReason(String failureReason) {
    this.failureReason = failureReason;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public LocalDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(LocalDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
