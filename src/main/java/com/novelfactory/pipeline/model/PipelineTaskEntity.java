package com.novelfactory.pipeline.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("pipeline_task")
public class PipelineTaskEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private PipelineTaskType taskType;
  private PipelineTaskStatus status;
  private TriggerSource triggerSource;
  private String resultMessage;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public PipelineTaskType getTaskType() {
    return taskType;
  }

  public void setTaskType(PipelineTaskType taskType) {
    this.taskType = taskType;
  }

  public PipelineTaskStatus getStatus() {
    return status;
  }

  public void setStatus(PipelineTaskStatus status) {
    this.status = status;
  }

  public TriggerSource getTriggerSource() {
    return triggerSource;
  }

  public void setTriggerSource(TriggerSource triggerSource) {
    this.triggerSource = triggerSource;
  }

  public String getResultMessage() {
    return resultMessage;
  }

  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
