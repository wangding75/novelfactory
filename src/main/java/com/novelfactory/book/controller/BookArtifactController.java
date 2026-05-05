package com.novelfactory.book.controller;

import com.novelfactory.common.api.ApiResponse;
import com.novelfactory.creative.model.AddictionCanvasResponse;
import com.novelfactory.creative.model.CharacterProfileListResponse;
import com.novelfactory.creative.model.WorldSettingResponse;
import com.novelfactory.creative.service.CreativeArtifactQueryService;
import com.novelfactory.outline.model.BookOutlineDraftResponse;
import com.novelfactory.outline.service.OutlineDraftQueryService;
import com.novelfactory.plan.model.BookPlanCardResponse;
import com.novelfactory.plan.service.PlanCardQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookArtifactController {
  private final CreativeArtifactQueryService creativeArtifactQueryService;
  private final PlanCardQueryService planCardQueryService;
  private final OutlineDraftQueryService outlineDraftQueryService;

  public BookArtifactController(
      CreativeArtifactQueryService creativeArtifactQueryService,
      PlanCardQueryService planCardQueryService,
      OutlineDraftQueryService outlineDraftQueryService) {
    this.creativeArtifactQueryService = creativeArtifactQueryService;
    this.planCardQueryService = planCardQueryService;
    this.outlineDraftQueryService = outlineDraftQueryService;
  }

  @GetMapping("/{bookId}/addiction-canvas/latest")
  public ApiResponse<AddictionCanvasResponse> getLatestAddictionCanvas(@PathVariable Long bookId) {
    return ApiResponse.ok(creativeArtifactQueryService.getLatestAddictionCanvas(bookId));
  }

  @GetMapping("/{bookId}/world-setting/latest")
  public ApiResponse<WorldSettingResponse> getLatestWorldSetting(@PathVariable Long bookId) {
    return ApiResponse.ok(creativeArtifactQueryService.getLatestWorldSetting(bookId));
  }

  @GetMapping("/{bookId}/character-profiles/latest")
  public ApiResponse<CharacterProfileListResponse> getLatestCharacterProfiles(
      @PathVariable Long bookId) {
    return ApiResponse.ok(creativeArtifactQueryService.getLatestCharacterProfiles(bookId));
  }

  @GetMapping("/{bookId}/plan-card/latest")
  public ApiResponse<BookPlanCardResponse> getLatestPlanCard(@PathVariable Long bookId) {
    return ApiResponse.ok(planCardQueryService.getLatestPlanCard(bookId));
  }

  @GetMapping("/{bookId}/outline-drafts/latest")
  public ApiResponse<BookOutlineDraftResponse> getLatestOutlineDraft(@PathVariable Long bookId) {
    return ApiResponse.ok(outlineDraftQueryService.getLatestOutlineDraft(bookId));
  }
}
