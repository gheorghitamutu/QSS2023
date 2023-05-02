package org.application.domain.models.validators.session;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {SessionValidator.class})
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidSession {
    String message() default "This session does not meet our requirements!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
