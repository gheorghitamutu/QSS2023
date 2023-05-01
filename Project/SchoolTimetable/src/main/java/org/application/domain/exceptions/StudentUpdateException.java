package org.application.domain.exceptions;

public class StudentUpdateException extends Exception{
    public StudentUpdateException(String message) {
        super(message);
    }

    public StudentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
