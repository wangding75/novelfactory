package com.novelfactory.pipeline.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.common.exception.GlobalExceptionHandler;
import com.novelfactory.common.exception.NotFoundException;
import com.novelfactory.pipeline.model.PipelineTaskResponse;
import com.novelfactory.pipeline.model.PipelineTaskStatus;
import com.novelfactory.pipeline.model.PipelineTaskType;
import com.novelfactory.pipeline.model.TriggerSource;
import com.novelfactory.pipeline.service.PipelineTaskApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PipelineTaskController.class)
@Import(GlobalExceptionHandler.class)
class PipelineTaskControllerWebTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PipelineTaskApplicationService pipelineTaskApplicationService;

  @Test
  void createPipelineTask_returnsTaskPayload() throws Exception {
    when(pipelineTaskApplicationService.createPipelineTask(any()))
        .thenReturn(new PipelineTaskResponse(
            1001L,
            1L,
            PipelineTaskType.BOOK_ONBOARDING,
            PipelineTaskStatus.COMPLETED,
            "stub execution completed"));

    mockMvc.perform(post("/api/v1/pipeline-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "bookId": 1,
                  "taskType": "BOOK_ONBOARDING",
                  "triggerSource": "MANUAL"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.taskId").value(1001L))
        .andExpect(jsonPath("$.data.status").value("COMPLETED"));
  }

  @Test
  void createPipelineTask_whenMissingBookId_returnsValidationError() throws Exception {
    mockMvc.perform(post("/api/v1/pipeline-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "taskType": "BOOK_ONBOARDING",
                  "triggerSource": "MANUAL"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()));
  }

  @Test
  void createPipelineTask_whenCreateFails_returnsPipelineTaskCreateFailed() throws Exception {
    when(pipelineTaskApplicationService.createPipelineTask(any()))
        .thenThrow(new BusinessException(ErrorCode.PIPELINE_TASK_CREATE_FAILED, "task create failed"));

    mockMvc.perform(post("/api/v1/pipeline-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "bookId": 1,
                  "taskType": "BOOK_ONBOARDING",
                  "triggerSource": "MANUAL"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.PIPELINE_TASK_CREATE_FAILED.name()));
  }

  @Test
  void getPipelineTask_whenMissing_returnsTaskNotFound() throws Exception {
    when(pipelineTaskApplicationService.getPipelineTask(999L))
        .thenThrow(new NotFoundException(ErrorCode.PIPELINE_TASK_NOT_FOUND, "task not found"));

    mockMvc.perform(get("/api/v1/pipeline-tasks/999"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.PIPELINE_TASK_NOT_FOUND.name()));
  }
}
