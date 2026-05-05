package com.novelfactory.pipeline.repository;

import com.novelfactory.pipeline.model.PipelineTaskEntity;
import java.util.Optional;

public interface PipelineTaskRepository {

  PipelineTaskEntity save(PipelineTaskEntity pipelineTaskEntity);

  Optional<PipelineTaskEntity> findById(Long taskId);
}
