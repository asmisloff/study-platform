package ru.asmisloff.studyplatform.dto;

import org.apache.commons.lang3.StringUtils;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.Constraints;

import java.util.function.Predicate;

public record LessonUpdateRequest(Long id, String title, String description, int index) {

    public LessonUpdateRequest(Long id, String title, String description, int index) {
        this.id = id;
        this.title = StringUtils.trim(title);
        this.description = StringUtils.trim(description);
        this.index = index;
    }

    public AbstractViolation validate(Predicate<Long> isExists) {
        return Constraints.useConstraints(this, "Запрос на изменение урока", true)
            .wrap(LessonSaveRequest.titleViolation(title))
            .wrap(LessonSaveRequest.descriptionViolation(description))
            .wrap(Constraints.useConstraints(id, "ID урока", true)
                .addRule("Должен присутствовать в БД", isExists.test(id))
            );
    }
}
