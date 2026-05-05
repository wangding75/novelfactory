package com.novelfactory.book.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBookRequest(
    @NotBlank String title,
    @NotBlank String genre,
    @NotBlank String platform,
    @NotNull BookStatus status,
    String description,
    String source) {}
