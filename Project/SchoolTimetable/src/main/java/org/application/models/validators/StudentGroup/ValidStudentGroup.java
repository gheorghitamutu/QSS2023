package org.application.models.validators.StudentGroup;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {StudentGroupValidator.class})
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidStudentGroup {
    String message() default "This student group does not meet our requirements!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
