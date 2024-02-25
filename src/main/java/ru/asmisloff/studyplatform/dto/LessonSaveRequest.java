package ru.asmisloff.studyplatform.dto;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import java.util.function.LongPredicate;

import static ru.asmisloff.studyplatform.validation.Constraints.defConstraints;
import static ru.asmisloff.studyplatform.validation.Constraints.useConstraints;

@Getter
public final class LessonSaveRequest {

    private String title;
    private String description;
    private long courseId;
    private int index;

    public AbstractViolation validate(LongPredicate isCourseExists) {
        return useConstraints(this, "Сохранение урока", true)
            .wrap(defConstraints(title, "Наименование", true, 1, 50))
            .wrap(defConstraints(description, "Описание", true, 1, 50))
            .wrap(
                useConstraints(courseId, "ID курса", true).addRule(
                    "Курс ID = %d не существует".formatted(courseId),
                    isCourseExists.test(courseId)
                )
            );
    }

    @SuppressWarnings("unused")
    private void setTitle(String title) {
        this.title = StringUtils.trim(title);
    }

    @SuppressWarnings("unused")
    private void setDescription(String description) {
        this.description = StringUtils.trim(description);
    }
}
