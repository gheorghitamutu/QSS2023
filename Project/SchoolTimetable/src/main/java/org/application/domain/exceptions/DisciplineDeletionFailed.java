package org.application.domain.exceptions;

public class DisciplineDeletionFailed extends Exception{
    public DisciplineDeletionFailed(String message) {
        super(message);
    }

    public DisciplineDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
