package org.article.util;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Nested;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for FileArticleLogger and LoggingException.
 */
@DisplayName("Logging Tests")
class FileArticleLoggerTest {
    @TempDir
    Path tempDir;
    private Path logFile;
    private FileArticleLogger logger;

    @BeforeEach
    void setUp() throws LoggingException {
        logFile = tempDir.resolve("test.log");
        logger = new FileArticleLogger(logFile.toString());
    }

    @AfterEach
    void tearDown() {
        if (logger != null) {
            logger.close();
        }
    }

    /**
     * Tests for the FileArticleLogger class.
     */
    @Nested
    @DisplayName("FileArticleLogger Tests")
    class FileArticleLoggerTests {
        /**
         * Tests logging of an info message.
         */
        @Test
        @DisplayName("Successfully logs warning message")
        void testWarningLog() throws IOException {
            String warningMessage = "Test warning message";
            logger.warning(warningMessage);
            logger.close();
            List<String> logLines = Files.readAllLines(logFile);
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains(warningMessage)),
                    "Log file should contain warning message");
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains("WARNING")),
                    "Log file should contain WARNING level");
        }

        /**
         * Tests logging of an error message with an exception.
         */
        @Test
        @DisplayName("Successfully logs error message with exception")
        void testErrorLog() throws IOException {
            String errorMessage = "Test error message";
            Exception testException = new RuntimeException("Test exception");
            logger.error(errorMessage, testException);
            logger.close();
            List<String> logLines = Files.readAllLines(logFile);
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains(errorMessage)),
                    "Log file should contain error message");
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains("SEVERE")),
                    "Log file should contain SEVERE level");
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains("Test exception")),
                    "Log file should contain exception message");
        }

        /**
         * Tests logging of an error message with a nested exception.
         */
        @Test
        @DisplayName("Logs error with nested exception")
        void testErrorLogWithNestedException() throws IOException {
            String errorMessage = "Test error message";
            Exception cause = new IllegalArgumentException("Root cause");
            Exception testException = new RuntimeException("Test exception", cause);
            logger.error(errorMessage, testException);
            logger.close();
            List<String> logLines = Files.readAllLines(logFile);
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains("Root cause")),
                    "Log file should contain cause message");
        }

        /**
         * Tests logging of an error message with a null exception.
         */
        @Test
        @DisplayName("Handles null exception message")
        void testErrorLogWithNullExceptionMessage() throws IOException {
            String errorMessage = "Test error message";
            Exception testException = new RuntimeException((Throwable) null);
            logger.error(errorMessage, testException);
            logger.close();
            List<String> logLines = Files.readAllLines(logFile);
            assertTrue(logLines.stream()
                            .anyMatch(line -> line.contains(errorMessage)),
                    "Log file should contain error message even with null exception message");
        }

        /**
         * Tests logging of an error message with a null message.
         */
        @Test
        @DisplayName("Creates new log file if it doesn't exist")
        void testCreateNewLogFile() throws LoggingException, IOException {
            Path newLogFile = tempDir.resolve("new.log");
            assertFalse(Files.exists(newLogFile), "Log file should not exist initially");
            try (FileArticleLogger newLogger = new FileArticleLogger(newLogFile.toString())) {
                newLogger.warning("Test message");
            }
            assertTrue(Files.exists(newLogFile), "Log file should be created");
            assertTrue(Files.size(newLogFile) > 0, "Log file should contain data");
        }

        /**
         * Tests appending to an existing log file.
         */
        @Test
        @DisplayName("Appends to existing log file")
        void testAppendToExistingLog() throws IOException, LoggingException {
            String firstMessage = "First message";
            String secondMessage = "Second message";
            logger.warning(firstMessage);
            logger.close();
            try (FileArticleLogger newLogger = new FileArticleLogger(logFile.toString())) {
                newLogger.warning(secondMessage);
            }
            List<String> logLines = Files.readAllLines(logFile);
            assertTrue(logLines.stream().anyMatch(line -> line.contains(firstMessage)),
                    "Log should contain first message");
            assertTrue(logLines.stream().anyMatch(line -> line.contains(secondMessage)),
                    "Log should contain second message");
        }
    }

    /**
     * Tests for the LoggingException class.
     */
    @Nested
    @DisplayName("LoggingException Tests")
    class LoggingExceptionTests {
        @Test
        @DisplayName("Creates exception with message")
        void testExceptionWithMessage() {
            String message = "Test error message";
            LoggingException exception = new LoggingException(message);
            assertEquals(message, exception.getMessage(),
                    "Exception message should match input message");
        }

        /**
         * Tests creation of a LoggingException with a message and cause.
         */
        @Test
        @DisplayName("Creates exception with message and cause")
        void testExceptionWithMessageAndCause() {
            String message = "Test error message";
            Throwable cause = new IOException("Test cause");
            LoggingException exception = new LoggingException(message, cause);
            assertAll(
                    () -> assertEquals(message, exception.getMessage(),
                            "Exception message should match input message"),
                    () -> assertEquals(cause, exception.getCause(),
                            "Exception cause should match input cause")
            );
        }

        /**
         * Tests creation of a LoggingException with a null message.
         */
        @Test
        @DisplayName("Handles null message")
        void testExceptionWithNullMessage() {
            assertDoesNotThrow(() -> new LoggingException(null),
                    "Should handle null message gracefully");
        }

        /**
         * Tests creation of a LoggingException with a null cause.
         */
        @Test
        @DisplayName("Handles null cause")
        void testExceptionWithNullCause() {
            assertDoesNotThrow(() -> new LoggingException("message", null),
                    "Should handle null cause gracefully");
        }
    }
}