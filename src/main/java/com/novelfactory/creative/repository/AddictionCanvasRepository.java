package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.AddictionCanvasEntity;
import java.util.Optional;

public interface AddictionCanvasRepository {

  AddictionCanvasEntity save(AddictionCanvasEntity entity);

  Optional<AddictionCanvasEntity> findLatestByBookId(Long bookId);
}
