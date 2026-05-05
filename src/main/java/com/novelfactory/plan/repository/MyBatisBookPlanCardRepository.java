package com.novelfactory.plan.repository;

import com.novelfactory.plan.model.BookPlanCardEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MyBatisBookPlanCardRepository implements BookPlanCardRepository {

  @Override
  public BookPlanCardEntity save(BookPlanCardEntity entity) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<BookPlanCardEntity> findLatestByBookId(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
