package ru.asmisloff.studyplatform.controller.request.parameter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.asmisloff.studyplatform.validation.AbstractViolation;
import ru.asmisloff.studyplatform.validation.Constraints;
import ru.asmisloff.studyplatform.validation.StringViolation;

@RequiredArgsConstructor
@JsonDeserialize(using = DatabaseIdDeserializer.class)
public class DatabaseID {

    private final String value;

    public boolean isNull() {
        return StringUtils.equalsIgnoreCase(value, "null");
    }

    public boolean isUndefined() {
        return StringUtils.equalsIgnoreCase(value, "undefined");
    }

    public long longValue() {
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
            this.value, "Параметр запроса %s".formatted(param), true, 0, Integer.MAX_VALUE
        );
        if (required) {
            v.addRule("Значение обязательно", !isUndefined());
        }
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
