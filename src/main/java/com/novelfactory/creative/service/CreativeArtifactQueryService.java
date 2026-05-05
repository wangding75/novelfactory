package com.novelfactory.creative.service;

import com.novelfactory.creative.model.AddictionCanvasResponse;
import com.novelfactory.creative.model.CharacterProfileListResponse;
import com.novelfactory.creative.model.WorldSettingResponse;

public interface CreativeArtifactQueryService {

  AddictionCanvasResponse getLatestAddictionCanvas(Long bookId);

  WorldSettingResponse getLatestWorldSetting(Long bookId);

  CharacterProfileListResponse getLatestCharacterProfiles(Long bookId);
}
