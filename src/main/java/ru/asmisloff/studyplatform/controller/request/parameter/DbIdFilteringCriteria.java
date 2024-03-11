package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.Constraints;
import ru.asmisloff.studyplatform.validation.StringViolation;

import java.io.IOException;

@RequiredArgsConstructor
@JsonDeserialize(using = DbIdFilteringCriteriaDeserializer.class)
public class DbIdFilteringCriteria {

    private final String value;

    public boolean isNull() {
        return StringUtils.equalsIgnoreCase(value, "null");
    }

    public boolean isUndefined() {
        return value == null;
    }

    public static final DbIdFilteringCriteria UNDEFINED = new DbIdFilteringCriteria(null);

    public long toBigInt() {
        if (isNull()) {
            return 0L;
        } else if (isUndefined()) {
            return -1L;
        } else {
            return Long.parseLong(value);
        }
    }

    public AbstractViolation validate(String param, boolean required, boolean nullable) {
        StringViolation v = Constraints.useConstraints(
            this.value, "Параметр запроса %s".formatted(param), required, 0, Integer.MAX_VALUE
        );
        if (!nullable) {
            v.addRule("Значение null не допускается", !isNull());
        }
        if (!isUndefined() && !isNull()) {
            v.addRule("Значение должно быть целым положительным числом не более %d".formatted(Long.MAX_VALUE), isValidNumber());
        }
        return v;
    }

    private boolean isValidNumber() {
        try {
            long n = Long.parseLong(value);
            return n > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


class DbIdFilteringCriteriaDeserializer extends JsonDeserializer<DbIdFilteringCriteria> {

    @Override
    public DbIdFilteringCriteria deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new DbIdFilteringCriteria(p.getText());
    }

    @Override
    public DbIdFilteringCriteria getNullValue(DeserializationContext ctxt) {
        return DbIdFilteringCriteria.UNDEFINED;
    }

    @Override
    public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
        return getNullValue(ctxt);
    }
}
