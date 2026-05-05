package com.novelfactory.pipeline.model;

public record PipelineTaskArtifactRefs(
    Long addictionCanvasId,
    Long worldSettingId,
    Long latestCharacterProfileBatchTaskId,
    Long planCardId,
    Long outlineDraftId) {

  public static PipelineTaskArtifactRefs empty() {
    return new PipelineTaskArtifactRefs(null, null, null, null, null);
  }
}
