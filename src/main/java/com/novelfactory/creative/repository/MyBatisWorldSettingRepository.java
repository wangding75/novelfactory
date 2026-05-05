package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.WorldSettingEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisWorldSettingRepository implements WorldSettingRepository {

  @Override
  public WorldSettingEntity save(WorldSettingEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<WorldSettingEntity> findLatestByBookId(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
