package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("VulnerableCodeUsages")
class DatabaseIDTest {

    private final ObjectMapper m = new ObjectMapper();

    @Test
    @SneakyThrows
    public void deserialization() {
        DatabaseID id = m.readValue("123", DatabaseID.class);
        assertEquals(123, id.longValue());
    }
}