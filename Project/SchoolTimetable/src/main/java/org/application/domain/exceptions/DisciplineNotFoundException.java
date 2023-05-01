package org.application.domain.exceptions;

public class DisciplineNotFoundException extends Exception{
    public DisciplineNotFoundException(String message) {
        super(message);
    }

    public DisciplineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
