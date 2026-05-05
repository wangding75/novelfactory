package com.novelfactory.creative.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("addiction_canvas")
public class AddictionCanvasEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private Long pipelineTaskId;
  private String coreEmotionalHook;
  private String emotionalFuel;
  private String readerFantasy;
  private String suppressionReleaseRhythm;
  private String forbiddenRules;
  private String rawContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getBookId() { return bookId; }
  public void setBookId(Long bookId) { this.bookId = bookId; }
  public Long getPipelineTaskId() { return pipelineTaskId; }
  public void setPipelineTaskId(Long pipelineTaskId) { this.pipelineTaskId = pipelineTaskId; }
  public String getCoreEmotionalHook() { return coreEmotionalHook; }
  public void setCoreEmotionalHook(String coreEmotionalHook) { this.coreEmotionalHook = coreEmotionalHook; }
  public String getEmotionalFuel() { return emotionalFuel; }
  public void setEmotionalFuel(String emotionalFuel) { this.emotionalFuel = emotionalFuel; }
  public String getReaderFantasy() { return readerFantasy; }
  public void setReaderFantasy(String readerFantasy) { this.readerFantasy = readerFantasy; }
  public String getSuppressionReleaseRhythm() { return suppressionReleaseRhythm; }
  public void setSuppressionReleaseRhythm(String suppressionReleaseRhythm) { this.suppressionReleaseRhythm = suppressionReleaseRhythm; }
  public String getForbiddenRules() { return forbiddenRules; }
  public void setForbiddenRules(String forbiddenRules) { this.forbiddenRules = forbiddenRules; }
  public String getRawContent() { return rawContent; }
  public void setRawContent(String rawContent) { this.rawContent = rawContent; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
