package org.domain.exceptions.teacher;

/**
 * This is the class for TeacherDeletionFailed.
 */
public class TeacherDeletionFailed extends Exception {

    /**
     * This is the constructor of TeacherDeletionFailed.
     * @param message The message.
     */
    public TeacherDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of TeacherDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public TeacherDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
