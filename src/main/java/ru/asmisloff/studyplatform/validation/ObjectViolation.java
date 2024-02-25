package ru.asmisloff.studyplatform.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ObjectViolation<T> extends AbstractViolation {

    protected @Nullable T value;

    public ObjectViolation(@Nullable T value, @NotNull String name) {
        this.value = value;
        this.name = Objects.requireNonNull(name);
    }

    public ObjectViolation<T> addRule(String msg, boolean condition) {
        if (value != null && !condition) {
            addError(msg);
        }
        return this;
    }

    @Override
    public String getTitle() {
        if (value != null) {
            return name;
        } else {
            return String.format("%s = значение отсутствует", name);
        }
    }
}
