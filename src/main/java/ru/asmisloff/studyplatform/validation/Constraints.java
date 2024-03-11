package ru.asmisloff.studyplatform.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
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
        StringViolation v = defConstraints(value, name, required, min, max, null, null);
        return requireNonNullElse(v, new StringViolation(value, name));
    }

    public static StringViolation defConstraints(String value, String name, boolean required, int min, int max) {
        return defConstraints(value, name, required, min, max, null, null);
    }

    public static StringViolation defConstraints(String value, String name, boolean required, String[] allowedValues) {
        return defConstraints(value, name, required, 0, Integer.MAX_VALUE, allowedValues, null);
    }

    public static StringViolation defConstraints(
        String value, String name, boolean required, int min, int max, String[] allowedValues, Function<String, String> additionally
    ) {
        StringViolation v = null;
        if (value == null) {
            if (required) {
                v = new StringViolation(null, name);
                v.addError("Параметр обязателен");
            }
        } else {
            int len = value.length();
            if (len < min || len > max) {
                v = new StringViolation(value, name);
                v.addError("Длина должна быть в диапазоне %d...%d".formatted(min, max));
            }
            if (allowedValues != null && !contains(allowedValues, value)) {
                v = requireNonNullElse(v, new StringViolation(value, name));
                v.addError("Неизвестное значение");
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

    public static @Nullable DoubleViolation defConstraints(
        double value,
        String name,
        int min,
        int max,
        int precision,
        @Nullable double[] allowedValues
    ) {
        return DoubleViolation.defConstraints(name, value, min, max, precision, allowedValues);
    }

    public static @NotNull DoubleViolation useConstraints(
        double value,
        String name,
        int min,
        int max,
        int precision,
        double[] allowedValues) {
        return Objects.requireNonNullElse(
            defConstraints(value, name, min, max, precision, allowedValues),
            new DoubleViolation(name, value)
        );
    }

    public static @Nullable AbstractViolation defConstraints(
        @Nullable Double value,
        String name,
        boolean required,
        int min,
        int max,
        int precision,
        @Nullable double[] allowedValues
    ) {
        if (value == null && required) {
            return new NullableDoubleViolation(null, name);
        } else if (value != null) {
            return DoubleViolation.defConstraints(name, value, min, max, precision, allowedValues);
        }
        return null;
    }

    @NotNull
    public static AbstractViolation useConstraints(
        @Nullable Double value,
        String name,
        boolean required,
        int min,
        int max,
        int precision,
        @Nullable double[] allowedValues
    ) {
        var v = defConstraints(value, name, required, min, max, precision, allowedValues);
        if (v != null) {
            return v;
        } else if (value != null) {
            return new DoubleViolation(name, value);
        } else {
            return new NullableDoubleViolation(null, name);
        }
    }

    public static boolean contains(String[] array, String elt) {
        for (String s : array) {
            if (elt.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
