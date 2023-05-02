package org.application.domain.exceptions.Timeslot;

public class TimeslotNotFoundException extends Exception{
    public TimeslotNotFoundException(String message) {
        super(message);
    }

    public TimeslotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
