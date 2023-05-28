package org.domain.exceptions.Timeslot;

/**
 * This is the class for TimeslotAdditionException.
 */
public class TimeslotAdditionException extends Exception {

    /**
     * This is the default constructor of TimeslotAdditionException.
     */
    public TimeslotAdditionException(String message) {
        super(message);
    }

    /**
     * This is the constructor of TimeslotAdditionException.
     * @param message The message.
     * @param cause The cause.
     */
    public TimeslotAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
