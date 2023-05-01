package org.application.domain.exceptions;

public class StudentGroupNotFoundException extends Exception{
    public StudentGroupNotFoundException(String message) {
        super(message);
    }

    public StudentGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
