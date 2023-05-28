package org.domain.exceptions.Timeslot;

/**
 * This is the class for TimeslotNotFoundException.
 */
public class TimeslotNotFoundException extends Exception {

    /**
     * This is the constructor of TimeslotNotFoundException.
     * @param message The message.
     */
    public TimeslotNotFoundException(String message) {
        super(message);
    }

    /**
     * This is the constructor of TimeslotNotFoundException.
     * @param message The message.
     * @param cause The cause.
     */
    public TimeslotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
