package org.domain.exceptions;

/**
 * This is the class for RepositoryOperationException.
 */
public class RepositoryOperationException extends Exception {

    /**
     * This is the constructor of RepositoryOperationException.
     * @param message The message.
     * @param cause The cause.
     */
    public RepositoryOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * This is the constructor of RepositoryOperationException.
     * @param message The message.
     */
    public RepositoryOperationException(String message) {
        super(message);
    }
}
