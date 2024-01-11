package ru.asmisloff.studyplatform.dto;

import ru.asmisloff.studyplatform.validation.TitleCase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CourseRequestToCreate(

    @NotNull
    @NotBlank(message = "Course author has to be filled")
    String author,

    @NotNull
    @NotBlank(message = "Course title has to be filled")
    @TitleCase(
        name = "Заголовок",
        message = "Неверное название",
        lang = TitleCase.Lang.Ru
    )
    String title
) { }
