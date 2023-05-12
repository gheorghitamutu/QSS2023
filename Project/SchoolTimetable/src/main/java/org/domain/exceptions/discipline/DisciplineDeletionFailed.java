package org.domain.exceptions.discipline;

public class DisciplineDeletionFailed extends Exception{
    public DisciplineDeletionFailed(String message) {
        super(message);
    }

    public DisciplineDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
