package org.domain.exceptions.session;

/**
 * This is the class for SessionAdditionException.
 */
public class SessionAdditionException extends Exception {

    /**
     * This is the constructor of SessionAdditionException.
     * @param message The message.
     */
    public SessionAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of SessionAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public SessionAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
