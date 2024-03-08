package ru.asmisloff.studyplatform.validation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import ru.asmisloff.studyplatform.controller.request.PagingAndSearchParameters;

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

    public static ObjectViolation<PagingAndSearchParameters> useConstraints(
        @NotNull PagingAndSearchParameters pagingAndSearchParameters,
        String[] allowedSortProperties
    ) {
        var v = useConstraints(pagingAndSearchParameters, "Пагинация и сортировка", false);
        for (String param : pagingAndSearchParameters.getSortBy()) {
            String field = StringUtils.substringBefore(param, ':');
            String direction = StringUtils.substringAfter(param, ':');
            boolean asc = StringUtils.compareIgnoreCase(direction, "asc") == 0;
            boolean desc = StringUtils.compareIgnoreCase(direction, "desc") == 0;
            StringViolation paramViolation = useConstraints(param, "sortBy", true, 0, Integer.MAX_VALUE);
            paramViolation.addRule("Неизвестное поле", contains(allowedSortProperties, field));
            paramViolation.addRule("Неизвестное направление сортировки", asc || desc || direction.isEmpty());
            v.wrap(paramViolation);
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
        String value, String name, boolean required, int min, int max, String[] allowedValues, Function<@NotNull String, String> additionally
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

    private static boolean contains(String[] array, String elt) {
        for (String s : array) {
            if (elt.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
