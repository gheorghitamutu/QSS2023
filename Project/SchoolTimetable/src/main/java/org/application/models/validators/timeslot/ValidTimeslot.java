package org.application.models.validators.timeslot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.application.models.validators.timeslot.TimeslotValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {TimeslotValidator.class})
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidTimeslot {
    String message() default "This timeslot does not meet our requirements!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}