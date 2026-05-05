package com.novelfactory.creative.service;

import com.novelfactory.creative.model.AddictionCanvasResponse;
import com.novelfactory.creative.model.CharacterProfileListResponse;
import com.novelfactory.creative.model.WorldSettingResponse;
import org.springframework.stereotype.Service;

@Service
public class DefaultCreativeArtifactQueryService implements CreativeArtifactQueryService {

  @Override
  public AddictionCanvasResponse getLatestAddictionCanvas(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public WorldSettingResponse getLatestWorldSetting(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public CharacterProfileListResponse getLatestCharacterProfiles(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
