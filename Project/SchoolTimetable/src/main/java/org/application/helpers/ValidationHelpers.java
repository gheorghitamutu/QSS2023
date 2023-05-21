package org.application.helpers;

import org.domain.exceptions.validations.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ValidationHelpers {


    public static <T> void requireNotNull(T object, Class<? extends Throwable> exception, String message, Throwable exceptionToWrap) throws ValidationException {

        if (object == null) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    public static void requirePositiveOrZero(Number object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.intValue() < 0) {
            throwException(exception, exceptionToWrap, message);
        }

    }


    public static void requirePositive(Number object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.intValue() <= 0) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    public static void requireNotBlank(String object, Class<? extends Throwable> exception, String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.isBlank()) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    public static void requireNotEmpty(String object, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (object.isEmpty()) {
            throwException(exception, exceptionToWrap, message);
        }
    }

    //require matches regex
    public static void requireMatchesRegex(String object, String regex, Class<? extends Throwable> exception,  String message, Throwable exceptionToWrap) throws ValidationException {

        if (!object.matches(regex)) {
            throwException(exception, exceptionToWrap, message);
        }
    }

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
