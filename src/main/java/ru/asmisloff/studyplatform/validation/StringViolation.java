package ru.asmisloff.studyplatform.validation;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class StringViolation extends ObjectViolation<String> {

    public StringViolation(@Nullable String value, @NotNull String name) {
        super(value, name);
    }

    @Override
    public String getTitle() {
        if (value != null) {
            return String.format("%s = %s", name, value);
        } else {
            return String.format("%s = значение отсутствует", name);
        }
    }
}
