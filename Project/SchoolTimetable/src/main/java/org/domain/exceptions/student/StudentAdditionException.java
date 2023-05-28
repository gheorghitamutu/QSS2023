package org.domain.exceptions.student;

/**
 * This is the class for StudentAdditionException.
 */
public class StudentAdditionException extends Exception {

    /**
     * This is the constructor of StudentAdditionException.
     * @param message The message.
     */
    public StudentAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
