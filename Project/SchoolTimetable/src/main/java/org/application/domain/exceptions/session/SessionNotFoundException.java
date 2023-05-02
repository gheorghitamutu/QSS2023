package org.application.domain.exceptions.session;

public class SessionNotFoundException extends Exception{
    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
