package ru.asmisloff.studyplatform.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LessonSaveRequestTest {

    private final String json = """
        {
            "title": " qwerty",
            "description": "uiop",
            "courseId": 1,
            "index": 2
        }
        """;

    private final ObjectMapper m = new ObjectMapper();

    @Test
    @SneakyThrows
    public void deserialization() {
        LessonSaveRequest request = m.readValue(json, LessonSaveRequest.class);
        assertEquals("qwerty", request.title());
    }
}