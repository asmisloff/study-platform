package ru.amisfloff.studyplatform.validator;

import jakarta.annotation.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Validator<T> {

    T defRule(String description, Predicate<T> predicate) {

    }

    T defRule(String description, boolean predicate) {

    }

    T defProhibition(String description, Predicate<T> predicate) {

    }

    T defProhibition(String description, boolean predicate) {

    }

    T wrap(Validator<?>... validator) {

    }

    void checkConstraints() {

    }

    @Nullable
    Violation getViolation() {

    }

    T note(Supplier<String> supplier) {

    }
}
