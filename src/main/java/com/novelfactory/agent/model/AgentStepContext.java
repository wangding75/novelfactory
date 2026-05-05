package com.novelfactory.agent.model;

import com.novelfactory.book.model.BookEntity;
import com.novelfactory.pipeline.model.GenerationTarget;
import com.novelfactory.pipeline.model.PipelineTaskStage;

public record AgentStepContext(
    Long pipelineTaskId,
    Long bookId,
    BookEntity book,
    GenerationTarget generationTarget,
    PipelineTaskStage currentStage) {}
