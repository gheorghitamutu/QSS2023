package org.application.domain.exceptions;

public class DisciplineAdditionException extends Exception{

    public DisciplineAdditionException(String message) {
        super(message);
    }

    public DisciplineAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
