package org.application.domain.exceptions.studentgroup;

public class StudentGroupReassignException extends Exception{
    public StudentGroupReassignException(String message) {
        super(message);
    }

    public StudentGroupReassignException(String message, Throwable cause) {
        super(message, cause);
    }
}
