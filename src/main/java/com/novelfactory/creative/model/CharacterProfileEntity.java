package com.novelfactory.creative.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("character_profile")
public class CharacterProfileEntity {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long bookId;
  private Long pipelineTaskId;
  private CharacterProfileRoleType roleType;
  private String name;
  private String roleLabel;
  private String background;
  private String motivation;
  private String personalityTraits;
  private String growthArc;
  private String rawContent;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Long getBookId() { return bookId; }
  public void setBookId(Long bookId) { this.bookId = bookId; }
  public Long getPipelineTaskId() { return pipelineTaskId; }
  public void setPipelineTaskId(Long pipelineTaskId) { this.pipelineTaskId = pipelineTaskId; }
  public CharacterProfileRoleType getRoleType() { return roleType; }
  public void setRoleType(CharacterProfileRoleType roleType) { this.roleType = roleType; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getRoleLabel() { return roleLabel; }
  public void setRoleLabel(String roleLabel) { this.roleLabel = roleLabel; }
  public String getBackground() { return background; }
  public void setBackground(String background) { this.background = background; }
  public String getMotivation() { return motivation; }
  public void setMotivation(String motivation) { this.motivation = motivation; }
  public String getPersonalityTraits() { return personalityTraits; }
  public void setPersonalityTraits(String personalityTraits) { this.personalityTraits = personalityTraits; }
  public String getGrowthArc() { return growthArc; }
  public void setGrowthArc(String growthArc) { this.growthArc = growthArc; }
  public String getRawContent() { return rawContent; }
  public void setRawContent(String rawContent) { this.rawContent = rawContent; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
