package org.domain.exceptions.Timeslot;

public class TimeslotAdditionException extends Exception{

    public TimeslotAdditionException(String message) {
        super(message);
    }

    public TimeslotAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
