package ru.asmisloff.studyplatform.dto;

import org.jetbrains.annotations.Nullable;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

public record ApiError(String message, String dateOccured, @Nullable AbstractViolation details) {
}
