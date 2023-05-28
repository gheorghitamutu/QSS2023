package org.domain.exceptions.studentgroup;

/**
 * This is the class for StudentGroupAdditionException.
 */
public class StudentGroupAdditionException extends Exception {

    /**
     * This is the constructor of StudentGroupAdditionException.
     * @param message The message.
     */
    public StudentGroupAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentGroupAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentGroupAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
