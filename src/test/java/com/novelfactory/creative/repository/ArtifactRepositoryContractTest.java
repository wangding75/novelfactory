package com.novelfactory.creative.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.novelfactory.creative.model.AddictionCanvasEntity;
import org.junit.jupiter.api.Test;

class ArtifactRepositoryContractTest {

  @Test
  void addictionCanvasRepository_saveReturnsPersistedEntity() {
    var repository = new MyBatisAddictionCanvasRepository();
    var entity = new AddictionCanvasEntity();
    entity.setBookId(1L);
    entity.setPipelineTaskId(1001L);
    entity.setCoreEmotionalHook("强烈反差");

    var saved = repository.save(entity);

    assertThat(saved.getBookId()).isEqualTo(1L);
  }
}
