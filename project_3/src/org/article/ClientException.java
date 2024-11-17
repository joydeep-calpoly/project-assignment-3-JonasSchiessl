package org.article;


/**
 * Exception thrown when the Client encounters errors during execution.
 */
public class ClientException extends Exception {
    /**
     * Constructs a new ClientException with the specified message.
     * @param message the error message
     */
    public ClientException(String message) {
        super(message);
    }

    /**
     * Constructs a new ClientException with the specified message and cause.
     * @param message the error message
     * @param cause the cause of the error
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
