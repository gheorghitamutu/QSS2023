package org.domain.exceptions.session;

/**
 * This is the class for SessionDeletionFailed.
 */
public class SessionDeletionFailed extends Exception {

    /**
     * This is the constructor of SessionDeletionFailed.
     * @param message The message.
     */
    public SessionDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of SessionDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public SessionDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
