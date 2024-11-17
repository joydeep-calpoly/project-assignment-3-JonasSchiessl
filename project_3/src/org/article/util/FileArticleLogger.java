package org.article.util;

import java.util.logging.*;

/**
 * File-based implementation of ArticleLogger.
 * Logs article-related messages to a specified file.
 * Implements AutoCloseable to ensure proper resource cleanup.
 */
public class FileArticleLogger implements ArticleLogger, AutoCloseable {
    private final Logger logger;
    private final FileHandler fileHandler;

    /**
     * Creates a new FileArticleLogger that writes to the specified file.
     * @param logFileName the name of the log file
     * @throws LoggingException if the logger cannot be configured
     */
    public FileArticleLogger(String logFileName) throws LoggingException {
        try {
            logger = Logger.getLogger(String.format("%s-%d",
                    FileArticleLogger.class.getName(),
                    System.currentTimeMillis()));
            fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            throw new LoggingException(
                    String.format("Failed to initialize file logger with file: %s", logFileName),
                    e);
        }
    }

    /**
     * Logs a warning message.
     * @param message the message to log
     */
    @Override
    public void warning(String message) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.warning(message);
        }
    }

    /**
     * Logs an error message with an exception.
     * @param message the message to log
     * @param e the exception to log
     */
    @Override
    public void error(String message, Exception e) {
        if (logger.isLoggable(Level.SEVERE)) {
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                logger.severe(String.format("%s: %s", message, errorMessage));
            } else {
                logger.severe(message);
            }

            Throwable cause = e.getCause();
            if (cause != null && cause.getMessage() != null) {
                logger.severe(String.format("Caused by: %s", cause.getMessage()));
            }
        }
    }

    /**
     * Closes the file handler and removes it from the logger.
     */
    @Override
    public void close() {
        fileHandler.close();
        logger.removeHandler(fileHandler);
    }
}