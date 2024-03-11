package ru.asmisloff.studyplatform.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.DoublePredicate;

import static ru.asmisloff.studyplatform.validation.Formatter.tr;

public class DoubleViolation extends AbstractViolation {

    private final double value;
    public static final double MAX_SAFE_JS_INTEGER = 9007199254740991.0;
    public static final double MIN_SAFE_JS_INTEGER = -9007199254740991.0;

    public DoubleViolation(@NotNull String name, double value) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getTitle() {
        return String.format("%s = %s", name, value);
    }

    public @NotNull DoubleViolation addRule(@NotNull String msg, boolean condition) {
        if (!condition) {
            addError(msg);
        }
        return this;
    }

    public @NotNull DoubleViolation addRule(@NotNull String msg, @NotNull DoublePredicate predicate) {
        return addRule(msg, predicate.test(value));
    }

    public @NotNull DoubleViolation addProhibition(@NotNull String msg, boolean condition) {
        if (condition) {
            addError(msg);
        }
        return this;
    }

    public @NotNull DoubleViolation addProhibition(@NotNull String msg, @NotNull DoublePredicate predicate) {
        return addProhibition(msg, predicate.test(value));
    }

    public static DoubleViolation defConstraints(
        String name,
        double val,
        double min,
        double max,
        int precision,
        @Nullable double[] allowedValues
    ) {
        var root = validateRange(name, val, min, max);
        root = validatePrecision(name, val, precision, root);
        if (allowedValues != null) {
            root = validateAllowedValues(name, val, allowedValues, root);
        }
        return root;
    }

    private static @Nullable DoubleViolation validateRange(String name, double val, double min, double max) {
        DoubleViolation v = null;
        if (val < MIN_SAFE_JS_INTEGER || val > MAX_SAFE_JS_INTEGER) {
            v = new DoubleViolation(name, val);
            v.addError("Некорректное значение");
        } else if (val < min || val > max) {
            v = new DoubleViolation(name, val);
            v.addError("Значение должно находиться в диапазоне %s...%s".formatted(tr(min), tr(max)));
        }
        return v;
    }

    private static @Nullable DoubleViolation validatePrecision(
        String name,
        double val,
        int precision,
        DoubleViolation root
    ) {
        if (precision < 0 || precision > 20) {
            throw new IllegalArgumentException("Некорректная точность: " + precision);
        }
        String s = String.valueOf(val);
        int dotIndex = s.indexOf('.');
        int numberOfFractionDigits = dotIndex >= 0 ? s.length() - dotIndex - 1 : 0;
        if (numberOfFractionDigits > precision) {
            root = Objects.requireNonNullElse(root, new DoubleViolation(name, val));
            root.addError("Значени должно содержать не более %d знаков после запятой".formatted(precision));
        }
        return root;
    }

    private static @Nullable DoubleViolation validateAllowedValues(
        String name,
        double val,
        @NotNull double[] allowedValues,
        DoubleViolation root
    ) {
        for (var v : allowedValues) {
            if (val == v) {
                return root;
            }
        }
        root = Objects.requireNonNullElse(root, new DoubleViolation(name, val));
        root.addError("Неизвестное значение");
        return root;
    }
}
