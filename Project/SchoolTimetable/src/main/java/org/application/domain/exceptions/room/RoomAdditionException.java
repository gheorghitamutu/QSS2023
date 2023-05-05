package org.application.domain.exceptions.room;

public class RoomAdditionException extends Exception{

    public RoomAdditionException(String message) {
        super(message);
    }

    public RoomAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
