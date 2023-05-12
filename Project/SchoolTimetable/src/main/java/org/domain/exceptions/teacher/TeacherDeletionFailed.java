package org.domain.exceptions.teacher;

public class TeacherDeletionFailed extends Exception{
    public TeacherDeletionFailed(String message) {
        super(message);
    }

    public TeacherDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
