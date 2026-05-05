package com.novelfactory.book.controller;

import com.novelfactory.book.model.BookDetailResponse;
import com.novelfactory.book.model.BookSummaryResponse;
import com.novelfactory.book.model.CreateBookRequest;
import com.novelfactory.book.service.BookApplicationService;
import com.novelfactory.common.api.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
  private final BookApplicationService bookApplicationService;

  public BookController(BookApplicationService bookApplicationService) {
    this.bookApplicationService = bookApplicationService;
  }

  @PostMapping
  public ApiResponse<BookDetailResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
    return ApiResponse.ok(bookApplicationService.createBook(request));
  }

  @GetMapping
  public ApiResponse<List<BookSummaryResponse>> listBooks(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String platform) {
    return ApiResponse.ok(bookApplicationService.listBooks(status, platform));
  }

  @GetMapping("/{bookId}")
  public ApiResponse<BookDetailResponse> getBook(@PathVariable Long bookId) {
    return ApiResponse.ok(bookApplicationService.getBookById(bookId));
  }
}
