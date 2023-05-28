package org.domain.exceptions.Timeslot;

/**
 * This is the class for TimeslotDeletionFailed.
 */
public class TimeslotDeletionFailed extends Exception {

    /**
     * This is the constructor of TimeslotDeletionFailed.
     * @param message The message.
     */
    public TimeslotDeletionFailed(String message) {
        super(message);
    }

    /**
     * This is the constructor of TimeslotDeletionFailed.
     * @param message The message.
     * @param cause The cause.
     */
    public TimeslotDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
