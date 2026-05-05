package com.novelfactory.pipeline.repository;

import com.novelfactory.pipeline.mapper.PipelineTaskMapper;
import com.novelfactory.pipeline.model.PipelineTaskEntity;
import java.util.Optional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisPipelineTaskRepository implements PipelineTaskRepository {
  private final PipelineTaskMapper pipelineTaskMapper;

  public MyBatisPipelineTaskRepository(ObjectProvider<PipelineTaskMapper> pipelineTaskMapperProvider) {
    this.pipelineTaskMapper = pipelineTaskMapperProvider.getIfAvailable();
  }

  @Override
  public PipelineTaskEntity save(PipelineTaskEntity pipelineTaskEntity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<PipelineTaskEntity> findById(Long taskId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
