package com.novelfactory.pipeline.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PipelineTaskModelContractTest {

  @Test
  void createPipelineTaskRequest_exposesGenerationTargetField() {
    var request =
        new CreatePipelineTaskRequest(
            1L, PipelineTaskType.BOOK_ONBOARDING, GenerationTarget.PLAN_CARD, TriggerSource.MANUAL);

    assertThat(request.bookId()).isEqualTo(1L);
    assertThat(request.generationTarget()).isEqualTo(GenerationTarget.PLAN_CARD);
  }

  @Test
  void pipelineTaskResponse_exposesStageFailureReasonAndArtifactRefs() {
    var response =
        new PipelineTaskResponse(
            1001L,
            1L,
            PipelineTaskType.BOOK_ONBOARDING,
            GenerationTarget.PLAN_CARD_AND_OUTLINE,
            PipelineTaskStatus.COMPLETED,
            PipelineTaskStage.COMPLETED,
            "done",
            null,
            new PipelineTaskArtifactRefs(11L, 12L, 13L, 14L, 15L));

    assertThat(response.currentStage()).isEqualTo(PipelineTaskStage.COMPLETED);
    assertThat(response.artifactRefs().outlineDraftId()).isEqualTo(15L);
  }
}
