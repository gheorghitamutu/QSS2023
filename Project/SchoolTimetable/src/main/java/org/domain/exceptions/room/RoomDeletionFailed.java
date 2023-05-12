package org.domain.exceptions.room;

public class RoomDeletionFailed extends Exception{
    public RoomDeletionFailed(String message) {
        super(message);
    }

    public RoomDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
