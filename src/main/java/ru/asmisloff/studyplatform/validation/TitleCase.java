package ru.asmisloff.studyplatform.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

