package org.domain.exceptions.studentgroup;

public class StudentGroupDeletionFailed extends Exception{
    public StudentGroupDeletionFailed(String message) {
        super(message);
    }

    public StudentGroupDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
