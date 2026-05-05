package com.novelfactory.creative.repository;

import com.novelfactory.creative.model.WorldSettingEntity;
import java.util.Optional;

public interface WorldSettingRepository {

  WorldSettingEntity save(WorldSettingEntity entity);

  Optional<WorldSettingEntity> findLatestByBookId(Long bookId);
}
