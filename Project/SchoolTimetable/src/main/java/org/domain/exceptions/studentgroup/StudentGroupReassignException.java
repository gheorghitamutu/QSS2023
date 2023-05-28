package org.domain.exceptions.studentgroup;

/**
 * This is the class for StudentGroupReassignException.
 */
public class StudentGroupReassignException extends Exception {

    /**
     * This is the constructor of StudentGroupReassignException.
     * @param message The message.
     */
    public StudentGroupReassignException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentGroupReassignException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentGroupReassignException(String message, Throwable cause) {
        super(message, cause);
    }
}
