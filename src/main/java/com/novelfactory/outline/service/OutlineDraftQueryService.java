package com.novelfactory.outline.service;

import com.novelfactory.outline.model.BookOutlineDraftResponse;

public interface OutlineDraftQueryService {

  BookOutlineDraftResponse getLatestOutlineDraft(Long bookId);
}
