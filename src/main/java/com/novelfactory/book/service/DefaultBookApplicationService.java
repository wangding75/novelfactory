package com.novelfactory.book.service;

import com.novelfactory.book.model.BookDetailResponse;
import com.novelfactory.book.model.BookSummaryResponse;
import com.novelfactory.book.model.CreateBookRequest;
import com.novelfactory.book.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultBookApplicationService implements BookApplicationService {
  private final BookRepository bookRepository;

  public DefaultBookApplicationService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public BookDetailResponse createBook(CreateBookRequest request) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<BookSummaryResponse> listBooks(String status, String platform) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public BookDetailResponse getBookById(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
