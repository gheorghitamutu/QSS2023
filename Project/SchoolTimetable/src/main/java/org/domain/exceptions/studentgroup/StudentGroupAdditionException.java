package org.domain.exceptions.studentgroup;

public class StudentGroupAdditionException extends Exception{

    public StudentGroupAdditionException(String message) {
        super(message);
    }

    public StudentGroupAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
