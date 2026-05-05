package com.novelfactory.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.book.repository.BookRepository;
import com.novelfactory.book.service.DefaultBookApplicationService;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.exception.GlobalExceptionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookController.class)
@Import({GlobalExceptionHandler.class, DefaultBookApplicationService.class})
class BookControllerWebTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookRepository bookRepository;

  @Test
  void createBook_returnsCreatedBookPayload() throws Exception {
    mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Files.readString(
                        Path.of("src/test/resources/requests/create-book-valid.json"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK"))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andExpect(jsonPath("$.data.title").value("测试书名"))
        .andExpect(jsonPath("$.data.source").value("MANUAL"));
  }

  @Test
  void createBook_withMissingTitle_returnsValidationError() throws Exception {
    mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    Files.readString(
                        Path.of("src/test/resources/requests/create-book-missing-title.json"))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()));
  }

  @Test
  void getBook_whenMissing_returnsBookNotFound() throws Exception {
    mockMvc.perform(get("/api/v1/books/404"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.BOOK_NOT_FOUND.name()));
  }

  @Test
  void listBooks_returnsSummaryList() throws Exception {
    mockMvc.perform(get("/api/v1/books").param("status", "DRAFT").param("platform", "FANQIE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value(1L))
        .andExpect(jsonPath("$.data[0].status").value("DRAFT"));
  }
}
