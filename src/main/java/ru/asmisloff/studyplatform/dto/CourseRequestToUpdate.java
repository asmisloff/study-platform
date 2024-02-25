package ru.asmisloff.studyplatform.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

public record CourseRequestToUpdate(Long id, String title) {

    @JsonCreator
    public CourseRequestToUpdate(Long id, String title) {
        this.id = id;
        this.title = StringUtils.trim(title);
    }

    public void validate() {

    }
}
