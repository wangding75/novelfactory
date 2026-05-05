package com.novelfactory.agent.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.novelfactory.agent.model.AgentExecutionRequest;
import org.junit.jupiter.api.Test;

class StubAgentOrchestratorTest {

  private final StubAgentOrchestrator orchestrator = new StubAgentOrchestrator();

  @Test
  void execute_returnsSuccessfulStubResult() {
    var result = orchestrator.execute(new AgentExecutionRequest(1L, "BOOK_ONBOARDING"));

    assertThat(result.success()).isTrue();
    assertThat(result.message()).isEqualTo("stub execution completed");
  }

  @Test
  void execute_withMissingTaskType_throwsIllegalArgumentException() {
    assertThatThrownBy(() -> orchestrator.execute(new AgentExecutionRequest(1L, null)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("taskType");
  }

  @Test
  void execute_withMissingBookId_throwsIllegalArgumentException() {
    assertThatThrownBy(() -> orchestrator.execute(new AgentExecutionRequest(null, "BOOK_ONBOARDING")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("bookId");
  }
}
