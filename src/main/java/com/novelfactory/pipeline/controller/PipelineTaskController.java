package com.novelfactory.pipeline.controller;

import com.novelfactory.common.api.ApiResponse;
import com.novelfactory.pipeline.model.CreatePipelineTaskRequest;
import com.novelfactory.pipeline.model.PipelineTaskResponse;
import com.novelfactory.pipeline.service.PipelineTaskApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pipeline-tasks")
public class PipelineTaskController {
  private final PipelineTaskApplicationService pipelineTaskApplicationService;

  public PipelineTaskController(PipelineTaskApplicationService pipelineTaskApplicationService) {
    this.pipelineTaskApplicationService = pipelineTaskApplicationService;
  }

  @PostMapping
  public ApiResponse<PipelineTaskResponse> createPipelineTask(
      @Valid @RequestBody CreatePipelineTaskRequest request) {
    return ApiResponse.ok(pipelineTaskApplicationService.createPipelineTask(request));
  }

  @GetMapping("/{taskId}")
  public ApiResponse<PipelineTaskResponse> getPipelineTask(@PathVariable Long taskId) {
    return ApiResponse.ok(pipelineTaskApplicationService.getPipelineTask(taskId));
  }
}
