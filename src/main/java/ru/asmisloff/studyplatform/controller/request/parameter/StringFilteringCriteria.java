package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

import java.io.IOException;

import static ru.asmisloff.studyplatform.validation.Constraints.defConstraints;

@RequiredArgsConstructor
@JsonDeserialize(using = StringFilteringCriteriaDeserializer.class)
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

    public AbstractViolation validate(String name, boolean required, int minLength, int maxLength) {
        return defConstraints(value, name, required, minLength, maxLength);
    }
}


class StringFilteringCriteriaDeserializer extends JsonDeserializer<StringFilteringCriteria> {

    @Override
    public StringFilteringCriteria deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new StringFilteringCriteria(p.getText());
    }
}