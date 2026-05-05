package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.CharacterProfileEntity;
import java.util.List;

public interface CharacterProfileRepository {

  CharacterProfileEntity save(CharacterProfileEntity entity);

  List<CharacterProfileEntity> findLatestByBookId(Long bookId);
}
