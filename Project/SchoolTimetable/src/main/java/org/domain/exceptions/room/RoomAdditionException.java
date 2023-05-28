package org.domain.exceptions.room;

/**
 * This is the class for RoomAdditionException.
 */
public class RoomAdditionException extends Exception {

    /**
     * This is the constructor of RoomAdditionException.
     * @param message The message.
     */
    public RoomAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of RoomAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public RoomAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
