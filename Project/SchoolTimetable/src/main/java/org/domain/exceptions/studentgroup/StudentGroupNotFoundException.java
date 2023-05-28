package org.domain.exceptions.studentgroup;

/**
 * This is the class for StudentGroupNotFoundException.
 */
public class StudentGroupNotFoundException extends Exception {

    /**
     * This is the constructor of StudentGroupNotFoundException.
     * @param message The message.
     */
    public StudentGroupNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentGroupNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
