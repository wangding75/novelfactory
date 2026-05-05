package com.novelfactory.outline.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("book_outline_draft")
public class BookOutlineDraftEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private Long pipelineTaskId;
  private String positioning;
  private String worldSummary;
  private String protagonistProfile;
  private String mainConflict;
  private String stagePlotOverview;
  private String arcStructure;
  private String rawContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getBookId() { return bookId; }
  public void setBookId(Long bookId) { this.bookId = bookId; }
  public Long getPipelineTaskId() { return pipelineTaskId; }
  public void setPipelineTaskId(Long pipelineTaskId) { this.pipelineTaskId = pipelineTaskId; }
  public String getPositioning() { return positioning; }
  public void setPositioning(String positioning) { this.positioning = positioning; }
  public String getWorldSummary() { return worldSummary; }
  public void setWorldSummary(String worldSummary) { this.worldSummary = worldSummary; }
  public String getProtagonistProfile() { return protagonistProfile; }
  public void setProtagonistProfile(String protagonistProfile) { this.protagonistProfile = protagonistProfile; }
  public String getMainConflict() { return mainConflict; }
  public void setMainConflict(String mainConflict) { this.mainConflict = mainConflict; }
  public String getStagePlotOverview() { return stagePlotOverview; }
  public void setStagePlotOverview(String stagePlotOverview) { this.stagePlotOverview = stagePlotOverview; }
  public String getArcStructure() { return arcStructure; }
  public void setArcStructure(String arcStructure) { this.arcStructure = arcStructure; }
  public String getRawContent() { return rawContent; }
  public void setRawContent(String rawContent) { this.rawContent = rawContent; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
