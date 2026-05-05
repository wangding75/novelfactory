package com.novelfactory.pipeline.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.agent.service.AgentOrchestrator;
import com.novelfactory.book.repository.BookRepository;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.GlobalExceptionHandler;
import com.novelfactory.pipeline.repository.PipelineTaskRepository;
import com.novelfactory.pipeline.service.DefaultPipelineTaskApplicationService;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PipelineTaskController.class)
@Import({GlobalExceptionHandler.class, DefaultPipelineTaskApplicationService.class})
class PipelineTaskControllerWebTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PipelineTaskRepository pipelineTaskRepository;
  @MockBean private BookRepository bookRepository;
  @MockBean private AgentOrchestrator agentOrchestrator;

  @Test
  void createPipelineTask_returnsTaskPayload() throws Exception {
    mockMvc.perform(
            post("/api/v1/pipeline-tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Files.readString(
                        Path.of(
                            "src/test/resources/requests/create-pipeline-task-plan-card.json"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.taskId").value(1001L))
        .andExpect(jsonPath("$.data.status").value("COMPLETED"))
        .andExpect(jsonPath("$.data.currentStage").value("COMPLETED"));
  }

  @Test
  void createPipelineTask_whenMissingBookId_returnsValidationError() throws Exception {
    mockMvc.perform(
            post("/api/v1/pipeline-tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Files.readString(
                        Path.of(
                            "src/test/resources/requests/create-pipeline-task-missing-book-id.json"))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()));
  }

  @Test
  void createPipelineTask_whenLlmCallFails_returnsLlmCallFailed() throws Exception {
    mockMvc.perform(
            post("/api/v1/pipeline-tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Files.readString(
                        Path.of("src/test/resources/requests/create-pipeline-task-outline.json"))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.LLM_CALL_FAILED.name()));
  }

  @Test
  void getPipelineTask_whenMissing_returnsTaskNotFound() throws Exception {
    mockMvc.perform(get("/api/v1/pipeline-tasks/999"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.PIPELINE_TASK_NOT_FOUND.name()));
  }
}
