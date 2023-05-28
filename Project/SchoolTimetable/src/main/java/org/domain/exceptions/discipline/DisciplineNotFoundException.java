package org.domain.exceptions.discipline;

/**
 * This is the class for DisciplineNotFoundException.
 */
public class DisciplineNotFoundException extends Exception {

    /**
     * This is the constructor of DisciplineNotFoundException.
     * @param message The message.
     */
    public DisciplineNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of DisciplineNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public DisciplineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
