package com.novelfactory.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.novelfactory.agent.model.AgentExecutionResult;
import com.novelfactory.agent.service.AgentOrchestrator;
import com.novelfactory.book.model.BookEntity;
import com.novelfactory.book.model.BookStatus;
import com.novelfactory.book.repository.BookRepository;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.common.exception.NotFoundException;
import com.novelfactory.pipeline.model.CreatePipelineTaskRequest;
import com.novelfactory.pipeline.model.PipelineTaskEntity;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;
import com.novelfactory.pipeline.repository.PipelineTaskRepository;
import com.novelfactory.pipeline.service.DefaultPipelineTaskApplicationService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PipelineTaskFlowIntegrationTest {

  @Mock
  private PipelineTaskRepository pipelineTaskRepository;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private AgentOrchestrator agentOrchestrator;

  private DefaultPipelineTaskApplicationService pipelineTaskApplicationService;

  @BeforeEach
  void setUp() {
    pipelineTaskApplicationService =
        new DefaultPipelineTaskApplicationService(
            pipelineTaskRepository, bookRepository, agentOrchestrator);
  }

  @Test
  void createPipelineTask_executesBookToPipelineToAgentFlow() {
    var book = new BookEntity();
    book.setId(1L);
    book.setTitle("测试书名");
    book.setStatus(BookStatus.DRAFT);

    var task = new PipelineTaskEntity();
    task.setId(1001L);
    task.setBookId(1L);
    task.setTaskType(PipelineTaskType.BOOK_ONBOARDING);
    task.setStatus(PipelineTaskStatus.COMPLETED);
    task.setTriggerSource(TriggerSource.MANUAL);
    task.setResultMessage("stub execution completed");

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(pipelineTaskRepository.save(any())).thenReturn(task);
    when(agentOrchestrator.execute(any())).thenReturn(new AgentExecutionResult(true, "stub execution completed"));

    var result = pipelineTaskApplicationService.createPipelineTask(
        new CreatePipelineTaskRequest(1L, PipelineTaskType.BOOK_ONBOARDING, TriggerSource.MANUAL));

    assertThat(result.taskId()).isEqualTo(1001L);
    assertThat(result.status()).isEqualTo(PipelineTaskStatus.COMPLETED);
  }

  @Test
  void createPipelineTask_whenBookMissing_throwsBookNotFound() {
    when(bookRepository.findById(404L)).thenReturn(Optional.empty());

    var exception = catchThrowableOfType(
        () -> pipelineTaskApplicationService.createPipelineTask(
            new CreatePipelineTaskRequest(404L, PipelineTaskType.BOOK_ONBOARDING, TriggerSource.MANUAL)),
        NotFoundException.class);

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOOK_NOT_FOUND);
  }

  @Test
  void createPipelineTask_whenAgentExecutionFails_throwsAgentExecutionFailed() {
    var book = new BookEntity();
    book.setId(1L);
    book.setStatus(BookStatus.DRAFT);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(agentOrchestrator.execute(any()))
        .thenThrow(new BusinessException(ErrorCode.AGENT_EXECUTION_FAILED, "agent execution failed"));

    var exception = catchThrowableOfType(
        () -> pipelineTaskApplicationService.createPipelineTask(
            new CreatePipelineTaskRequest(1L, PipelineTaskType.BOOK_ONBOARDING, TriggerSource.MANUAL)),
        BusinessException.class);

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.AGENT_EXECUTION_FAILED);
  }

  @Test
  void getPipelineTask_whenMissing_throwsPipelineTaskNotFound() {
    when(pipelineTaskRepository.findById(1001L)).thenReturn(Optional.empty());

    var exception = catchThrowableOfType(
        () -> pipelineTaskApplicationService.getPipelineTask(1001L),
        NotFoundException.class);

    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PIPELINE_TASK_NOT_FOUND);
  }
}
