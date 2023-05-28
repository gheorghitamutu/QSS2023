package org.domain.models.validators.timeslot;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This is an interface for TimeslotValidator annotation.
 */
@Constraint(validatedBy = {TimeslotValidator.class})
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidTimeslot {

    /**
     * This method returns the message.
     * @return The message.
     */
    String message() default "This timeslot does not meet our requirements!";

    /**
     * This method returns the groups.
     * @return The groups.
     */
    Class<?>[] groups() default {};

    /**
     * This method returns the payload.
     * @return The payload.
     */
    Class<? extends Payload>[] payload() default {};
}
