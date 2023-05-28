package org.domain.exceptions.studentgroup;

/**
 * This is the class for StudentGroupDeletionFailed.
 */
public class StudentGroupDeletionFailed extends Exception {

    /**
     * This is the constructor of StudentGroupDeletionFailed.
     * @param message The message.
     */
    public StudentGroupDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of StudentGroupDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public StudentGroupDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
