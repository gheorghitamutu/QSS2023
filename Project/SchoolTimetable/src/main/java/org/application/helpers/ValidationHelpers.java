package org.application.helpers;

import org.domain.exceptions.validations.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Represents a helper class for validating objects.
 */
public class ValidationHelpers {

    /**
     * Checks if the given object is null.
     * @param object The object to check.
     * @param exception The exception to throw if the object is null.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @param <T> The type of the object to check.
     * @throws ValidationException If the object is null.
     */
    public static <T> void requireNotNull(T object, Class<? extends Throwable> exception, String message, Throwable exceptionToWrap) throws ValidationException {

        if (object == null) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    /**
     * Checks if the object is positive or zero.
     * @param object The object to check.
     * @param exception The exception to throw if the object is not positive or zero.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @throws ValidationException If the object is not positive or zero.
     */
    public static void requirePositiveOrZero(Number object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.intValue() < 0) {
            throwException(exception, exceptionToWrap, message);
        }

    }

    /**
     * Checks if the object is positive.
     * @param object The object to check.
     * @param exception The exception to throw if the object is not positive.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @throws ValidationException If the object is not positive.
     */
    public static void requirePositive(Number object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.intValue() <= 0) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    /**
     * Checks if the object is blank.
     * @param object The object to check.
     * @param exception The exception to throw if the object is blank.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @throws ValidationException If the object is blank.
     */
    public static void requireNotBlank(String object, Class<? extends Throwable> exception, String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.isBlank()) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    /**
     * Checks if the object is empty.
     * @param object The object to check.
     * @param exception The exception to throw if the object is empty.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @throws ValidationException If the object is empty.
     */
    public static void requireNotEmpty(String object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.isEmpty()) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    /**
     * Check if the object matches the regex.
     * @param object The object to check.
     * @param regex The regex to check against.
     * @param exception The exception to throw if the object does not match the regex.
     * @param message The message to pass to the exception.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @throws ValidationException If the object does not match the regex.
     */
    public static void requireMatchesRegex(String object, String regex, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (!object.matches(regex)) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    /**
     * Throws an exception.
     * @param exception The exception to throw.
     * @param exceptionToWrap The exception to wrap in the exception to throw.
     * @param message The message to pass to the exception.
     * @throws ValidationException The exception to throw.
     */
    private static void throwException(Class<? extends Throwable> exception, Throwable exceptionToWrap, String message) throws ValidationException {
        Throwable exceptionToThrow = null;

        try {
            if (exceptionToWrap == null) {

                //use the constructor that only takes a message

                //instantiate exception using reflection

                exceptionToThrow = exception.getConstructor(String.class).newInstance(message);
            } else {

                //use the constructor that takes a message and a cause
                exceptionToThrow = exception.getConstructor(String.class, Throwable.class).newInstance(message, exceptionToWrap);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

            return;
        }

        throw new ValidationException("Validation fails", exceptionToThrow);
    }
}
