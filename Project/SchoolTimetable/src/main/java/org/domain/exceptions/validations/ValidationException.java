package org.domain.exceptions.validations;

public class ValidationException extends Exception{

    public ValidationException() {
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
