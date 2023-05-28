package org.domain.exceptions.teacher;

/**
 * This is the class for TeacherNotFoundException.
 */
public class TeacherNotFoundException extends Exception {

    /**
     * This is the constructor of TeacherNotFoundException.
     * @param message The message.
     */
    public TeacherNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of TeacherNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public TeacherNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
