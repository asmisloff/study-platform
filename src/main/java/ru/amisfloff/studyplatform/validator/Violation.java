package ru.amisfloff.studyplatform.validator;

import java.util.List;

public record Violation(
    String name,
    List<String> what,
    List<Violation> nested
) { }
