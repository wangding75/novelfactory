package com.novelfactory.agent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;
import org.junit.jupiter.api.Test;

class StubAgentOrchestratorTest {

  private final StubAgentOrchestrator orchestrator = new StubAgentOrchestrator();

  @Test
  void execute_returnsSuccessfulStubResult() {
    var result =
        orchestrator.execute(
            new AgentExecutionRequest(
                1001L,
                1L,
                PipelineTaskType.BOOK_ONBOARDING,
                GenerationTarget.PLAN_CARD,
                TriggerSource.MANUAL));

    assertThat(result.success()).isTrue();
    assertThat(result.status()).isEqualTo(PipelineTaskStatus.COMPLETED);
    assertThat(result.currentStage()).isEqualTo(PipelineTaskStage.COMPLETED);
    assertThat(result.message()).isEqualTo("stub execution completed");
  }

  @Test
  void execute_withMissingTaskType_throwsIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                orchestrator.execute(
                    new AgentExecutionRequest(
                        1001L, 1L, null, GenerationTarget.PLAN_CARD, TriggerSource.MANUAL)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("taskType");
  }

  @Test
  void execute_withMissingBookId_throwsIllegalArgumentException() {
    assertThatThrownBy(
            () ->
                orchestrator.execute(
                    new AgentExecutionRequest(
                        1001L,
                        null,
                        PipelineTaskType.BOOK_ONBOARDING,
                        GenerationTarget.PLAN_CARD,
                        TriggerSource.MANUAL)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("bookId");
  }
}
