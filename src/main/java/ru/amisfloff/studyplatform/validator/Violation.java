package ru.amisfloff.studyplatform.validator;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Violation(
    @NotNull String name,
    @NotNull List<String> what,
    @Nullable List<Violation> nested
) { }
