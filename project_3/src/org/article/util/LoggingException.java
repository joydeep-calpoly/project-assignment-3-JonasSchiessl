package org.article.util;

/**
 * Custom exception for logging-related errors.
 * Provides specific handling for logging configuration and operation failures.
 */
public class LoggingException extends Exception {
  /**
   * Constructs a new logging exception with the specified message.
   * @param message the error message
   */
  @SuppressWarnings("unused")
  public LoggingException(String message) {
    super(message);
  }

  /**
   * Constructs a new logging exception with the specified message and cause.
   * @param message the error message
   * @param cause the cause of the error
   */
  public LoggingException(String message, Throwable cause) {
    super(message, cause);
  }
}
