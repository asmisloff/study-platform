package ru.amisfloff.studyplatform.validation;

import java.util.function.Supplier;

public class Violation extends TerminatingViolation {

    public Violation(String title) {
        super(title);
    }

    public Violation addRule(boolean condition, Supplier<String> description) {
        if (!condition) {
            getOrCreateWhat().add(description.get());
        }
        return this;
    }

    public Violation addProhibition(boolean condition, Supplier<String> description) {
        return addRule(!condition, description);
    }

    public Violation wrap(Violation other) {
        if (other != null && (other.what != null && !other.what.isEmpty() || other.nested != null && !other.nested.isEmpty())) {
            getOrCreateNested().add(other);
        }
        return this;
    }
}
