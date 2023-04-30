package org.application.domain.exceptions;

public class RepositoryOperationException extends Exception {

    public RepositoryOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public RepositoryOperationException(String message) {
        super(message);
    }
}
