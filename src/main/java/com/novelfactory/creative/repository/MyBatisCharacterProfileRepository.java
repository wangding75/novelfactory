package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.CharacterProfileEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisCharacterProfileRepository implements CharacterProfileRepository {

  @Override
  public CharacterProfileEntity save(CharacterProfileEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public List<CharacterProfileEntity> findLatestByBookId(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
