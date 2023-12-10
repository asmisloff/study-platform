package ru.amisfloff.studyplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.Character.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = TitleCaseConstraintValidator.class)
public @interface TitleCase {

    Lang lang() default Lang.Any;

    String name();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Lang {En, Ru, Any}
}

