package org.domain.exceptions.session;

/**
 * This is the class for SessionNotFoundException.
 */
public class SessionNotFoundException extends Exception {

    /**
     * This is the constructor of SessionNotFoundException.
     * @param message The message.
     */
    public SessionNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of SessionNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
