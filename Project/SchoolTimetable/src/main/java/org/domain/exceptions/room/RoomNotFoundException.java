package org.domain.exceptions.room;

/**
 * This is the class for RoomNotFoundException.
 */
public class RoomNotFoundException extends Exception {

    /**
     * This is the constructor of RoomNotFoundException.
     * @param message The message.
     */
    public RoomNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of RoomNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
