package com.novelfactory.book.service;

import com.novelfactory.book.model.BookDetailResponse;
import com.novelfactory.book.model.BookSummaryResponse;
import com.novelfactory.book.model.CreateBookRequest;
import java.util.List;

public interface BookApplicationService {

  BookDetailResponse createBook(CreateBookRequest request);

  List<BookSummaryResponse> listBooks(String status, String platform);

  BookDetailResponse getBookById(Long bookId);
}
