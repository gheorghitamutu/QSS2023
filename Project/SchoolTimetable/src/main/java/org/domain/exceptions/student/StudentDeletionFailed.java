package org.domain.exceptions.student;

/**
 * This is the class for StudentDeletionFailed.
 */
public class StudentDeletionFailed extends Exception {

    /**
     * This is the constructor of StudentDeletionFailed.
     * @param message The message.
     */
    public StudentDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
