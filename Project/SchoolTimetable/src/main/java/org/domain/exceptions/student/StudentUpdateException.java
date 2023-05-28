package org.domain.exceptions.student;

/**
 * This is the class for StudentUpdateException.
 */
public class StudentUpdateException extends Exception {

    /**
     * This is the constructor of StudentUpdateException.
     * @param message The message.
     */
    public StudentUpdateException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentUpdateException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
