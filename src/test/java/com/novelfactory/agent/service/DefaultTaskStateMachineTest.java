package com.novelfactory.agent.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskEvent;
import com.novelfactory.pipeline.model.PipelineTaskStage;
import org.junit.jupiter.api.Test;

class DefaultTaskStateMachineTest {

  private final DefaultTaskStateMachine stateMachine = new DefaultTaskStateMachine();

  @Test
  void transit_movesCreatedToRunningOnStartEvent() {
    var next =
        stateMachine.transit(
            PipelineTaskStage.CREATED, PipelineTaskEvent.START, GenerationTarget.PLAN_CARD);

    assertThat(next).isEqualTo(PipelineTaskStage.RUNNING);
  }

  @Test
  void transit_movesPlanCardDoneToCompletedWhenOutlineNotRequired() {
    var next =
        stateMachine.transit(
            PipelineTaskStage.PLAN_CARD_DONE,
            PipelineTaskEvent.COMPLETE_WITHOUT_OUTLINE,
            GenerationTarget.PLAN_CARD);

    assertThat(next).isEqualTo(PipelineTaskStage.COMPLETED);
  }
}
