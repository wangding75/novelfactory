package com.novelfactory.outline.repository;

import com.novelfactory.outline.model.BookOutlineDraftEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisBookOutlineDraftRepository implements BookOutlineDraftRepository {

  @Override
  public BookOutlineDraftEntity save(BookOutlineDraftEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<BookOutlineDraftEntity> findLatestByBookId(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
