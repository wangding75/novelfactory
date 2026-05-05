package com.novelfactory.book.model;

public record BookDetailResponse(
    Long id,
    String title,
    String genre,
    String platform,
    BookStatus status,
    String description,
    String source) {}
