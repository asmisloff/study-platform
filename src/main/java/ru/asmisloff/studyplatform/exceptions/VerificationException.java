package ru.asmisloff.studyplatform.exceptions;

import lombok.Getter;
import ru.asmisloff.studyplatform.validation.AbstractViolation;

@Getter
public class VerificationException extends RuntimeException {

    private final AbstractViolation violation;

    public VerificationException(AbstractViolation violation) {
        super("Некоторые данные не соответствуют ограничениям");
        this.violation = violation;
    }
}
