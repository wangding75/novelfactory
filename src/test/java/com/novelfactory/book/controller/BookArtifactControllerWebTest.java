package com.novelfactory.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novelfactory.common.exception.GlobalExceptionHandler;
import com.novelfactory.creative.service.DefaultCreativeArtifactQueryService;
import com.novelfactory.outline.service.DefaultOutlineDraftQueryService;
import com.novelfactory.plan.service.DefaultPlanCardQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BookArtifactController.class)
@Import({
  GlobalExceptionHandler.class,
  DefaultCreativeArtifactQueryService.class,
  DefaultPlanCardQueryService.class,
  DefaultOutlineDraftQueryService.class
})
class BookArtifactControllerWebTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void getLatestAddictionCanvas_returnsArtifactPayload() throws Exception {
    mockMvc.perform(get("/api/v1/books/1/addiction-canvas/latest"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.bookId").value(1L));
  }

  @Test
  void getLatestPlanCard_returnsPlanCardPayload() throws Exception {
    mockMvc.perform(get("/api/v1/books/1/plan-card/latest"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.bookId").value(1L));
  }
}
