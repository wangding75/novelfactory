package com.novelfactory.agent.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskArtifactRefs;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;
import org.junit.jupiter.api.Test;

class AgentExecutionContractTest {

  @Test
  void requestCarriesTaskContextAndGenerationTarget() {
    var request =
        new AgentExecutionRequest(
            1001L,
            1L,
            PipelineTaskType.BOOK_ONBOARDING,
            GenerationTarget.PLAN_CARD_AND_OUTLINE,
            TriggerSource.MANUAL);

    assertThat(request.pipelineTaskId()).isEqualTo(1001L);
    assertThat(request.generationTarget()).isEqualTo(GenerationTarget.PLAN_CARD_AND_OUTLINE);
  }

  @Test
  void resultCarriesFailureReasonAndArtifactRefs() {
    var result =
        new AgentExecutionResult(
            false,
            PipelineTaskStatus.FAILED,
            PipelineTaskStage.WORLD_SETTING_DONE,
            "world failed",
            "provider timeout",
            new PipelineTaskArtifactRefs(11L, 12L, null, null, null));

    assertThat(result.failureReason()).isEqualTo("provider timeout");
    assertThat(result.artifactRefs().worldSettingId()).isEqualTo(12L);
  }
}
