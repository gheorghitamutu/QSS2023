package org.domain.exceptions.student;

public class StudentDeletionFailed extends Exception{
    public StudentDeletionFailed(String message) {
        super(message);
    }

    public StudentDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
