package com.novelfactory.plan.service;

import com.novelfactory.plan.model.BookPlanCardResponse;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlanCardQueryService implements PlanCardQueryService {

  @Override
  public BookPlanCardResponse getLatestPlanCard(Long bookId) {
    throw new UnsupportedOperationException("not implemented");
  }
}
