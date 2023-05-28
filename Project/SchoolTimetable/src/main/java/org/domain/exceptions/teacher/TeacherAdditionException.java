package org.domain.exceptions.teacher;

/**
 * This is the class for TeacherAdditionException.
 */
public class TeacherAdditionException extends Exception {

    /**
     * This is the constructor of TeacherAdditionException.
     * @param message The message.
     */
    public TeacherAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of TeacherAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public TeacherAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
