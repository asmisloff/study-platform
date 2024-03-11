package ru.asmisloff.studyplatform.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullableDoubleViolation extends ObjectViolation<Double> {

    public NullableDoubleViolation(@Nullable Double value, @NotNull String name) {
        super(value, name);
    }

    @Override
    public String getTitle() {
        if (value == null) {
            return String.format("%s = <нет значения>", name);
        } else {
            return String.format("%s = %s", name, value);
        }
    }
}
