package com.novelfactory.plan.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("book_plan_card")
public class BookPlanCardEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private Long pipelineTaskId;
  private String title;
  private String genre;
  private String platform;
  private String oneLineHook;
  private String coreSetting;
  private String targetReaders;
  private String coreConflictSummary;
  private String creativeDirectionSummary;
  private String rawContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getBookId() { return bookId; }
  public void setBookId(Long bookId) { this.bookId = bookId; }
  public Long getPipelineTaskId() { return pipelineTaskId; }
  public void setPipelineTaskId(Long pipelineTaskId) { this.pipelineTaskId = pipelineTaskId; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getGenre() { return genre; }
  public void setGenre(String genre) { this.genre = genre; }
  public String getPlatform() { return platform; }
  public void setPlatform(String platform) { this.platform = platform; }
  public String getOneLineHook() { return oneLineHook; }
  public void setOneLineHook(String oneLineHook) { this.oneLineHook = oneLineHook; }
  public String getCoreSetting() { return coreSetting; }
  public void setCoreSetting(String coreSetting) { this.coreSetting = coreSetting; }
  public String getTargetReaders() { return targetReaders; }
  public void setTargetReaders(String targetReaders) { this.targetReaders = targetReaders; }
  public String getCoreConflictSummary() { return coreConflictSummary; }
  public void setCoreConflictSummary(String coreConflictSummary) { this.coreConflictSummary = coreConflictSummary; }
  public String getCreativeDirectionSummary() { return creativeDirectionSummary; }
  public void setCreativeDirectionSummary(String creativeDirectionSummary) { this.creativeDirectionSummary = creativeDirectionSummary; }
  public String getRawContent() { return rawContent; }
  public void setRawContent(String rawContent) { this.rawContent = rawContent; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
