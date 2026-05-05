package com.novelfactory.plan.repository;

import com.novelfactory.plan.model.BookPlanCardEntity;
import java.util.Optional;

public interface BookPlanCardRepository {

  BookPlanCardEntity save(BookPlanCardEntity entity);

  Optional<BookPlanCardEntity> findLatestByBookId(Long bookId);
}
