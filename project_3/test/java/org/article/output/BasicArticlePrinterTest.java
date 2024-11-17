package org.article.output;


import org.article.core.Article;
import org.article.core.Source;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for BasicArticlePrinter.
 * Verifies the output formatting of articles including:
 * - Basic formatting
 * - Handling of null values
 * - Line spacing
 */
@DisplayName("BasicArticlePrinter Tests")
class BasicArticlePrinterTest {
    private BasicArticlePrinter printer;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        printer = new BasicArticlePrinter();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests basic article printing format.
     * Verifies that the output matches the expected format.
     */
    @Test
    @DisplayName("Prints article in correct format")
    void testPrint() {
        // Arrange
        Article article = new Article(
                "Test Title",
                "Test Description",
                "2024-01-01",
                "https://test.com",
                null,
                null,
                new Source("test-id", "Test Source"),
                null
        );

        // Act
        printer.print(article);

        // Assert
        String expected = String.format(
                "title: Test Title%n" +
                        "at: 2024-01-01%n" +
                        "url: https://test.com%n" +
                        "Test Description%n%n"
        );

        assertEquals(expected, outputStream.toString(),
                "Printed output should match expected format");
    }

    /**
     * Tests printing of article with null fields.
     * Verifies that null values are handled appropriately.
     */
    @Test
    @DisplayName("Handles null fields appropriately")
    void testPrintWithNullFields() {
        // Arrange
        Article article = new Article(
                null, null, null, null, null, null, null, null
        );

        // Act
        printer.print(article);

        // Assert
        String expected = String.format(
                "title: N/A%n" +
                        "at: N/A%n" +
                        "url: N/A%n" +
                        "N/A%n%n"
        );

        assertEquals(expected, outputStream.toString(),
                "Null fields should be replaced with N/A");
    }
}
