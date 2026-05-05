package com.novelfactory.pipeline.service;

import com.novelfactory.agent.model.AgentExecutionRequest;
import com.novelfactory.agent.model.AgentExecutionResult;
import com.novelfactory.agent.service.AgentOrchestrator;
import com.novelfactory.book.model.BookEntity;
import com.novelfactory.book.repository.BookRepository;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.common.exception.NotFoundException;
import com.novelfactory.pipeline.model.CreatePipelineTaskRequest;
import com.novelfactory.pipeline.model.PipelineTaskEntity;
import com.novelfactory.pipeline.model.PipelineTaskResponse;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.repository.PipelineTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPipelineTaskApplicationService implements PipelineTaskApplicationService {
  private final PipelineTaskRepository pipelineTaskRepository;
  private final BookRepository bookRepository;
  private final AgentOrchestrator agentOrchestrator;

  public DefaultPipelineTaskApplicationService(
      PipelineTaskRepository pipelineTaskRepository,
      BookRepository bookRepository,
      AgentOrchestrator agentOrchestrator) {
    this.pipelineTaskRepository = pipelineTaskRepository;
    this.bookRepository = bookRepository;
    this.agentOrchestrator = agentOrchestrator;
  }

  @Override
  public PipelineTaskResponse createPipelineTask(CreatePipelineTaskRequest request) {
    BookEntity book =
        bookRepository
            .findById(request.bookId())
            .orElseThrow(
                () -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND, "book not found: " + request.bookId()));

    PipelineTaskEntity task = new PipelineTaskEntity();
    task.setBookId(book.getId());
    task.setTaskType(request.taskType());
    task.setTriggerSource(request.triggerSource());
    task.setStatus(PipelineTaskStatus.CREATED);

    PipelineTaskEntity savedTask = persistTask(task);

    try {
      AgentExecutionResult executionResult =
          executePipelineTask(
              new AgentExecutionRequest(savedTask.getBookId(), savedTask.getTaskType().name()));

      savedTask.setStatus(executionResult.success() ? PipelineTaskStatus.COMPLETED : PipelineTaskStatus.FAILED);
      savedTask.setResultMessage(executionResult.message());
      PipelineTaskEntity updatedTask = persistTask(savedTask);
      return toResponse(updatedTask);
    } catch (BusinessException exception) {
      savedTask.setStatus(PipelineTaskStatus.FAILED);
      savedTask.setResultMessage(exception.getMessage());
      tryPersistFailureStatus(savedTask);
      throw exception;
    }
  }

  @Override
  public PipelineTaskResponse getPipelineTask(Long taskId) {
    return pipelineTaskRepository
        .findById(taskId)
        .map(this::toResponse)
        .orElseThrow(
            () ->
                new NotFoundException(
                    ErrorCode.PIPELINE_TASK_NOT_FOUND, "pipeline task not found: " + taskId));
  }

  public AgentExecutionResult executePipelineTask(AgentExecutionRequest request) {
    return agentOrchestrator.execute(request);
  }

  private PipelineTaskEntity persistTask(PipelineTaskEntity taskEntity) {
    try {
      PipelineTaskEntity savedTask = pipelineTaskRepository.save(taskEntity);
      return savedTask != null ? savedTask : taskEntity;
    } catch (BusinessException exception) {
      throw exception;
    } catch (RuntimeException exception) {
      throw new BusinessException(
          ErrorCode.PIPELINE_TASK_CREATE_FAILED, "pipeline task save failed");
    }
  }

  private void tryPersistFailureStatus(PipelineTaskEntity taskEntity) {
    try {
      pipelineTaskRepository.save(taskEntity);
    } catch (RuntimeException ignored) {
      // Preserve the original business exception from the execution path.
    }
  }

  private PipelineTaskResponse toResponse(PipelineTaskEntity taskEntity) {
    return new PipelineTaskResponse(
        taskEntity.getId(),
        taskEntity.getBookId(),
        taskEntity.getTaskType(),
        taskEntity.getStatus(),
        taskEntity.getResultMessage());
  }
}
