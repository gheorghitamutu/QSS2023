package org.domain.exceptions.discipline;

/**
 * This is the class for DisciplineNotFoundException.
 */
public class DisciplineDeletionFailed extends Exception {

    /**
     * This is the constructor of DisciplineNotFoundException.
     * @param message The message.
     */
    public DisciplineDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of DisciplineNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public DisciplineDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
