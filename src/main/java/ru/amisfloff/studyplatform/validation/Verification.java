package ru.amisfloff.studyplatform.validation;

public class Verification {

    public static Violation useConstraints(int value, String title, int min, int max) {
        assert max > min;
        Violation v = new Violation(title);
        if (value < min || value > max) {
            v.getOrCreateWhat().add("Значение должно быть в диапазоне [%d...%d]".formatted(min, max));
        }
        return v;
    }

    public static Violation useConstraints(double value, String title, double min, double max) {
        assert max > min;
        Violation v = new Violation(title);
        if (value < min || value > max) {
            v.getOrCreateWhat().add("Значение должно быть в диапазоне [%f...%f]".formatted(min, max));
        }
        return v;
    }

    public static Violation useConstraints(String value, String title, int min, int max, boolean required) {
        assert min >= 0;
        assert max > min;
        Violation v = new Violation(title);
        if (value == null && required) {
            v.getOrCreateWhat().add("Значение обязательно");
        }
        if (value != null) {
            if (value.length() < min || value.length() > max) {
                v.getOrCreateWhat().add("Длина должна быть в диапазоне [%d...%d]".formatted(min, max));
            }
        }
        return v;
    }

    public static <T> Violation useConstraints(T value, String title, boolean required) {
        Violation v = new Violation(title);
        if (value == null && required) {
            v.getOrCreateWhat().add("Значение обязательно");
        }
        return v;
    }
}
