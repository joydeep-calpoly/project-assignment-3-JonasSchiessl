package org.article.parser;


public class ParserException extends Exception {
    /**
     * Constructs a ParserException with the provided message.
     *
     * @param message the message to include in the exception
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Constructs a ParserException with the provided message and cause.
     *
     * @param message the message to include in the exception
     * @param cause the cause of the exception
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
