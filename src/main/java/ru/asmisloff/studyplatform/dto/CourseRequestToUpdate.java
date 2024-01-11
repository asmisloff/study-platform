package ru.asmisloff.studyplatform.dto;

import ru.asmisloff.studyplatform.validation.TitleCase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CourseRequestToUpdate(

    @NotNull(message = "ID is required")
    Long id,

    @NotNull(message = "Author is required")
    @NotBlank(message = "Course author has to be filled")
    String author,

    @NotNull(message = "Title is required")
    @NotBlank(message = "Course title has to be filled")
    @TitleCase(
        name = "Заголовок",
        message = "Неверное название",
        lang = TitleCase.Lang.Ru
    )
    String title
) { }
