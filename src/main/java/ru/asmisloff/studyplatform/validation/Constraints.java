package ru.asmisloff.studyplatform.validation;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

import static java.util.Objects.requireNonNullElse;

public class Constraints {

    public static <T> ObjectViolation<T> useConstraints(@Nullable T value, @NotNull String name, boolean required) {
        ObjectViolation<T> v = new ObjectViolation<>(value, name);
        if (value == null) {
            if (required) {
                v.getWhat().add("Отсутствует значение");
            }
        }
        return v;
    }

    public static @NotNull StringViolation useConstraints(
        String value, String name, boolean required, int min, int max
    ) {
        StringViolation v = defConstraints(value, name, required, min, max, null);
        return requireNonNullElse(v, new StringViolation(value, name));
    }

    public static StringViolation defConstraints(String value, String name, boolean required, int min, int max) {
        return defConstraints(value, name, required, min, max, null);
    }

    public static StringViolation defConstraints(
        String value, String name, boolean required, int min, int max, Function<@NotNull String, String> additionally
    ) {
        StringViolation v = null;
        if (value == null) {
            if (required) {
                v = new StringViolation(null, name);
                v.getWhat().add("Параметр обязателен");
            }
        } else {
            int len = value.length();
            if (len < min || len > max) {
                v = new StringViolation(value, name);
                v.getWhat().add("Длина должна быть в диапазоне %d...%d".formatted(min, max));
            }
            if (additionally != null) {
                @Nullable String error = additionally.apply(value);
                if (error != null) {
                    v = requireNonNullElse(v, new StringViolation(value, name));
                    v.addError(error);
                }
            }
        }
        return v;
    }
}
