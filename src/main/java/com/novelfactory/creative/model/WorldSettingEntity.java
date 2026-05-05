package com.novelfactory.creative.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("world_setting")
public class WorldSettingEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private Long pipelineTaskId;
  private String positioning;
  private String worldSummary;
  private String coreRules;
  private String factionStructure;
  private String forbiddenItems;
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
  public String getCoreRules() { return coreRules; }
  public void setCoreRules(String coreRules) { this.coreRules = coreRules; }
  public String getFactionStructure() { return factionStructure; }
  public void setFactionStructure(String factionStructure) { this.factionStructure = factionStructure; }
  public String getForbiddenItems() { return forbiddenItems; }
  public void setForbiddenItems(String forbiddenItems) { this.forbiddenItems = forbiddenItems; }
  public String getRawContent() { return rawContent; }
  public void setRawContent(String rawContent) { this.rawContent = rawContent; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
