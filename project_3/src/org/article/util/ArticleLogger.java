package org.article.util;
/**
 * Interface for article logging operations.
 */
public interface ArticleLogger {
    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    void warning(String message);
    /**
     * Logs an error message.
     *
     * @param message the message to log
     * @param e the exception that caused the error
     */
    void error(String message, Exception e);
}
