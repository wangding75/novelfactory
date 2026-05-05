package com.novelfactory.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.book.model.BookDetailResponse;
import com.novelfactory.book.model.BookStatus;
import com.novelfactory.book.model.BookSummaryResponse;
import com.novelfactory.book.service.BookApplicationService;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.GlobalExceptionHandler;
import com.novelfactory.common.exception.NotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerWebTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookApplicationService bookApplicationService;

  @Test
  void createBook_returnsCreatedBookPayload() throws Exception {
    when(bookApplicationService.createBook(any()))
        .thenReturn(new BookDetailResponse(1L, "测试书名", "都市系统流", "FANQIE", BookStatus.DRAFT, "立项说明"));

    mockMvc.perform(post("/api/v1/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "title": "测试书名",
                  "genre": "都市系统流",
                  "platform": "FANQIE",
                  "status": "DRAFT",
                  "description": "立项说明"
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK"))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andExpect(jsonPath("$.data.title").value("测试书名"));
  }

  @Test
  void createBook_withMissingTitle_returnsValidationError() throws Exception {
    mockMvc.perform(post("/api/v1/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "genre": "都市系统流",
                  "platform": "FANQIE",
                  "status": "DRAFT",
                  "description": "立项说明"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()));
  }

  @Test
  void getBook_whenMissing_returnsBookNotFound() throws Exception {
    when(bookApplicationService.getBookById(404L))
        .thenThrow(new NotFoundException(ErrorCode.BOOK_NOT_FOUND, "book not found"));

    mockMvc.perform(get("/api/v1/books/404"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.BOOK_NOT_FOUND.name()));
  }

  @Test
  void listBooks_returnsSummaryList() throws Exception {
    when(bookApplicationService.listBooks("DRAFT", "FANQIE"))
        .thenReturn(List.of(new BookSummaryResponse(1L, "测试书名", "FANQIE", BookStatus.DRAFT)));

    mockMvc.perform(get("/api/v1/books")
            .param("status", "DRAFT")
            .param("platform", "FANQIE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value(1L))
        .andExpect(jsonPath("$.data[0].status").value("DRAFT"));
  }
}
