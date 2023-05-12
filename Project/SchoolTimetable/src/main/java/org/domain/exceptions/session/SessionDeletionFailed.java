package org.domain.exceptions.session;

public class SessionDeletionFailed extends Exception{
    public SessionDeletionFailed(String message) {
        super(message);
    }

    public SessionDeletionFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
