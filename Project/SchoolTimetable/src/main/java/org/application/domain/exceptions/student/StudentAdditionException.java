package org.application.domain.exceptions.student;

public class StudentAdditionException extends Exception{

    public StudentAdditionException(String message) {
        super(message);
    }

    public StudentAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
