package com.novelfactory.creative.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArtifactQueryServiceContractTest {

  private final DefaultCreativeArtifactQueryService creativeArtifactQueryService =
      new DefaultCreativeArtifactQueryService();

  @Test
  void getLatestAddictionCanvas_returnsStructuredArtifactResponse() {
    var response = creativeArtifactQueryService.getLatestAddictionCanvas(1L);

    assertThat(response.bookId()).isEqualTo(1L);
    assertThat(response.coreEmotionalHook()).isNotBlank();
  }
}
