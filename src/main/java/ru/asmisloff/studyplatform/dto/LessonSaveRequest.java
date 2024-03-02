package ru.asmisloff.studyplatform.dto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.StringViolation;

import java.util.function.Predicate;

import static ru.asmisloff.studyplatform.validation.Constraints.defConstraints;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

public record LessonSaveRequest(String title, String description, long courseId, int index) {

    public LessonSaveRequest(String title, String description, long courseId, int index) {
        this.title = StringUtils.trim(title);
        this.description = StringUtils.trim(description);
        this.courseId = courseId;
        this.index = index;
    }

    public AbstractViolation validate(Predicate<Long> isCourseExists) {
        return useConstraints(this, "Сохранение урока", true)
            .wrap(titleViolation(title))
            .wrap(descriptionViolation(description))
            .wrap(courseIdViolation(courseId, isCourseExists));
    }

    public static @Nullable AbstractViolation titleViolation(String title) {
        return defConstraints(title, "Наименование", true, 1, 50);
    }

    public static @Nullable StringViolation descriptionViolation(String description) {
        return defConstraints(description, "Описание", true, 1, 50);
    }

    public static @Nullable AbstractViolation courseIdViolation(Long courseId, Predicate<Long> isCourseExists) {
        return useConstraints(courseId, "ID курса", true)
            .addRule("Должен присутствовать в БД", isCourseExists.test(courseId));
    }
}
