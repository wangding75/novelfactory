package com.novelfactory.book.repository;

import com.novelfactory.book.model.BookEntity;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

  BookEntity save(BookEntity bookEntity);

  List<BookEntity> findAll(String status, String platform);

  Optional<BookEntity> findById(Long bookId);
}
