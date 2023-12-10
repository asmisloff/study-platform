package ru.amisfloff.studyplatform.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TerminatingViolation {

    public TerminatingViolation(String title) {
        this.title = title;
    }

    List<String> getOrCreateWhat() {
        if (what == null) what = new ArrayList<>();
        return what;
    }

    List<Violation> getOrCreateNested() {
        if (nested == null) nested = new ArrayList<>();
        return nested;
    }

    @Getter
    private final String title;
    @Getter
    protected List<String> what;
    @Getter
    protected List<Violation> nested;
}
