package org.domain.exceptions.student;

/**
 * This is the class for StudentNotFoundException.
 */
public class StudentNotFoundException extends Exception {

    /**
     * This is the constructor of StudentNotFoundException.
     * @param message The message.
     */
    public StudentNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
