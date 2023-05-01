package org.application.domain.exceptions;

public class StudentAdditionException extends Exception{

    public StudentAdditionException(String message) {
        super(message);
    }

    public StudentAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
