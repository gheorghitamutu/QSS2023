package org.domain.exceptions.teacher;

public class TeacherAdditionException extends Exception{

    public TeacherAdditionException(String message) {
        super(message);
    }

    public TeacherAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
