package org.domain.exceptions.discipline;

/**
 * This is the class for DisciplineAdditionException.
 */
public class DisciplineAdditionException extends Exception {

    /**
     * This is the constructor of DisciplineAdditionException.
     * @param message The message.
     */
    public DisciplineAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of DisciplineAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public DisciplineAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
