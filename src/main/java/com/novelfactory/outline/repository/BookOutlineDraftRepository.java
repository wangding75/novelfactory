package com.novelfactory.outline.repository;

import com.novelfactory.outline.model.BookOutlineDraftEntity;
import java.util.Optional;

public interface BookOutlineDraftRepository {

  BookOutlineDraftEntity save(BookOutlineDraftEntity entity);

  Optional<BookOutlineDraftEntity> findLatestByBookId(Long bookId);
}
