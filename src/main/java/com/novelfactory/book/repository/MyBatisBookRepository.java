package com.novelfactory.book.repository;

import com.novelfactory.book.mapper.BookMapper;
import com.novelfactory.book.model.BookEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.ObjectProvider;

@Repository
public class MyBatisBookRepository implements BookRepository {
  private final BookMapper bookMapper;

  public MyBatisBookRepository(ObjectProvider<BookMapper> bookMapperProvider) {
    this.bookMapper = bookMapperProvider.getIfAvailable();
  }

  @Override
  public BookEntity save(BookEntity bookEntity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<BookEntity> findAll(String status, String platform) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<BookEntity> findById(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
