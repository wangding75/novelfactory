package com.novelfactory.plan.service;

import com.novelfactory.plan.model.BookPlanCardResponse;

public interface PlanCardQueryService {

  BookPlanCardResponse getLatestPlanCard(Long bookId);
}
