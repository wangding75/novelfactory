package com.novelfactory.outline.service;

import com.novelfactory.outline.model.BookOutlineDraftResponse;
import org.springframework.stereotype.Service;

@Service
public class DefaultOutlineDraftQueryService implements OutlineDraftQueryService {

  @Override
  public BookOutlineDraftResponse getLatestOutlineDraft(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
