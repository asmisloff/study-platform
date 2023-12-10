package ru.amisfloff.studyplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.amisfloff.studyplatform.validation.TitleCase;

public record CourseRequestToCreate(
    @NotNull
    @NotBlank(message = "Course author has to be filled")
    String author,

    @NotNull
    @NotBlank(message = "Course title has to be filled")
    @TitleCase(name = "Заголовок", message = "Неверное название", lang = TitleCase.Lang.Ru)
    String title,

    @NotNull
    @NotBlank(message = "Course name has to be filled")
    String name
) { }
