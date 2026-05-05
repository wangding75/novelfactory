package com.novelfactory.common;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.book.controller.BookController;
import com.novelfactory.book.model.BookStatus;
import com.novelfactory.book.service.BookApplicationService;
import com.novelfactory.common.api.ErrorCode;
import com.novelfactory.common.controller.SystemController;
import com.novelfactory.common.exception.BusinessException;
import com.novelfactory.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {SystemController.class, BookController.class})
@Import(GlobalExceptionHandler.class)
class CommonWebContractTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookApplicationService bookApplicationService;

  @Test
  void ping_returnsUnifiedSuccessResponse() throws Exception {
    mockMvc.perform(get("/api/v1/system/ping"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("OK"))
        .andExpect(jsonPath("$.message").value("success"))
        .andExpect(jsonPath("$.data").value("pong"));
  }

  @Test
  void validationFailure_returnsValidationErrorEnvelope() throws Exception {
    mockMvc.perform(post("/api/v1/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "genre": "都市系统流",
                  "platform": "FANQIE",
                  "status": "DRAFT"
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()))
        .andExpect(jsonPath("$.message").value("validation failed"));
  }

  @Test
  void internalBusinessException_returnsConfiguredErrorCode() throws Exception {
    when(bookApplicationService.createBook(any()))
        .thenThrow(new BusinessException(ErrorCode.BOOK_CREATE_FAILED, "book create failed"));

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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", equalTo(ErrorCode.BOOK_CREATE_FAILED.name())))
        .andExpect(jsonPath("$.message", equalTo("book create failed")));
  }

  @Test
  void unexpectedException_returnsInternalErrorEnvelope() throws Exception {
    when(bookApplicationService.createBook(any()))
        .thenThrow(new RuntimeException("unexpected"));

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
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.code", equalTo(ErrorCode.INTERNAL_ERROR.name())))
        .andExpect(jsonPath("$.message", equalTo("unexpected")));
  }
}
