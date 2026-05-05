package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.AddictionCanvasEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisAddictionCanvasRepository implements AddictionCanvasRepository {

  @Override
  public AddictionCanvasEntity save(AddictionCanvasEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<AddictionCanvasEntity> findLatestByBookId(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
