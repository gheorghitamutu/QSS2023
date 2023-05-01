package org.application.domain.models.validators.teacher;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {TeacherValidator.class})
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidTeacher {
    String message() default "This teacher does not meet our requirements!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
