package com.novelfactory.creative.model;

import java.util.List;

public record CharacterProfileListResponse(
    Long bookId, Long pipelineTaskId, List<CharacterProfileResponse> profiles) {}
