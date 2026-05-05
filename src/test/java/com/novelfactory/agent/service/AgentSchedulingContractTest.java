package com.novelfactory.agent.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;
import org.junit.jupiter.api.Test;

class AgentSchedulingContractTest {

  private final DefaultAgentScheduler scheduler = new DefaultAgentScheduler();

  @Test
  void run_returnsCompletedExecutionForPlanCardTask() {
    var result =
        scheduler.run(
            new AgentExecutionRequest(
                1001L,
                1L,
                PipelineTaskType.BOOK_ONBOARDING,
                GenerationTarget.PLAN_CARD,
                TriggerSource.MANUAL));

    assertThat(result.success()).isTrue();
    assertThat(result.status()).isEqualTo(PipelineTaskStatus.COMPLETED);
    assertThat(result.currentStage()).isEqualTo(PipelineTaskStage.COMPLETED);
  }
}
