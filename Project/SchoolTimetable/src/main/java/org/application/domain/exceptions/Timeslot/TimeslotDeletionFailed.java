package org.application.domain.exceptions.Timeslot;

public class TimeslotDeletionFailed extends Exception{
    public TimeslotDeletionFailed(String message) {
        super(message);
    }

    public TimeslotDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
