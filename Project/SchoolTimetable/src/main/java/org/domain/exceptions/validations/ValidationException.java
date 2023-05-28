package org.domain.exceptions.validations;

/**
 * This is the class for ValidationException.
 */
public class ValidationException extends Exception{

    /**
     * This is the default constructor of ValidationException.
     */
    public ValidationException() {
    }

    /**
     * This is the constructor of ValidationException.
     * @param message The message.
     * @param cause The cause.
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
