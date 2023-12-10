package ru.amisfloff.studyplatform.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.logging.Logger;

class TitleCaseConstraintValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = { "ew123" })
    public void test(String s) {
        Set<ConstraintViolation<Title>> violations = validator.validate(new Title(new RuString(s)));
        violations.forEach(System.out::println);
    }

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Logger logger = Logger.getLogger(TitleCaseConstraintValidatorTest.class.getSimpleName());

    private record Title(@Valid RuString ruString) { }

    private record RuString(@TitleCase(name = "Заголовок", message = "", lang = TitleCase.Lang.Ru) String s) { }
}