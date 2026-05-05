package com.novelfactory.common.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ErrorCodeContractTest {

  @Test
  void errorCode_containsAllGenerationAndArtifactErrors() {
    var names = Arrays.stream(ErrorCode.values()).map(Enum::name).toList();

    assertThat(names)
        .contains(
            "INVALID_GENERATION_TARGET",
            "LLM_CALL_FAILED",
            "ADDICTION_CANVAS_GENERATION_FAILED",
            "WORLD_SETTING_GENERATION_FAILED",
            "CHARACTER_PROFILE_GENERATION_FAILED",
            "PLAN_CARD_GENERATION_FAILED",
            "OUTLINE_GENERATION_FAILED",
            "ADDICTION_CANVAS_NOT_FOUND",
            "WORLD_SETTING_NOT_FOUND",
            "CHARACTER_PROFILE_NOT_FOUND",
            "PLAN_CARD_NOT_FOUND",
            "OUTLINE_DRAFT_NOT_FOUND");
  }
}
