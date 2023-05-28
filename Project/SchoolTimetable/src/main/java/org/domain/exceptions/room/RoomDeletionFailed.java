package org.domain.exceptions.room;

/**
 * This is the class for RoomDeletionFailed.
 */
public class RoomDeletionFailed extends Exception {

    /**
     * This is the constructor of RoomDeletionFailed.
     * @param message The message.
     */
    public RoomDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of RoomDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public RoomDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
