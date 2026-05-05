package com.novelfactory.book.model;

public record BookSummaryResponse(Long id, String title, String platform, BookStatus status) {}
