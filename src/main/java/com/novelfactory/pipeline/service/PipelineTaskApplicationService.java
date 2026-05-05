package com.novelfactory.pipeline.service;

import com.novelfactory.pipeline.model.CreatePipelineTaskRequest;
import com.novelfactory.pipeline.model.PipelineTaskResponse;

public interface PipelineTaskApplicationService {

  PipelineTaskResponse createPipelineTask(CreatePipelineTaskRequest request);

  PipelineTaskResponse getPipelineTask(Long taskId);
}
