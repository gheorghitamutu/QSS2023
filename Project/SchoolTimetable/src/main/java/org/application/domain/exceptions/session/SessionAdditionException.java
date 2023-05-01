package org.application.domain.exceptions.session;

public class SessionAdditionException extends Exception{

    public SessionAdditionException(String message) {
        super(message);
    }

    public SessionAdditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
