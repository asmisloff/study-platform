package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class StringFilteringCriteria {

    private final String value;

    public boolean isNull() {
        return StringUtils.equalsIgnoreCase(value, "null");
    }

    public boolean isUndefined() {
        return value == null;
    }

    public String toVarchar() {
        if (isUndefined()) {
            return "\t";
        } else {
            return value;
        }
    }

    public static final StringFilteringCriteria UNDEFINED = new StringFilteringCriteria(null);
}


class StringFilteringCriteriaDeserializer extends JsonDeserializer<StringFilteringCriteria> {

    @Override
    public StringFilteringCriteria deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new StringFilteringCriteria(p.getText());
    }
}