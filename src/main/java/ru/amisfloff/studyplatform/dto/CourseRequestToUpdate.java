package ru.amisfloff.studyplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequestToUpdate(
    @NotNull Long id,
    @NotNull @NotBlank String author,
    @NotNull @NotBlank String title
) { }
