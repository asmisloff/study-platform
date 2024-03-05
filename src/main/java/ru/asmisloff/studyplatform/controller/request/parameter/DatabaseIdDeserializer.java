package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class DatabaseIdDeserializer extends JsonDeserializer<DatabaseID> {

    @Override
    public DatabaseID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) {
            throw new JsonParseException(p, "Отсутствует значение параметра DatabaseId");
        }
        return new DatabaseID(value);
    }
}
