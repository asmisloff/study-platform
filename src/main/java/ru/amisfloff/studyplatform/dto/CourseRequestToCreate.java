package ru.amisfloff.studyplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequestToCreate(
    @NotNull @NotBlank String author,
    @NotNull @NotBlank String title,
    @NotNull @NotBlank String name
) { }
