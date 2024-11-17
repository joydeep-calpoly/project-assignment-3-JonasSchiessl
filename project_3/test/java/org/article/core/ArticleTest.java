package org.article.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Article class.
 * Verifies the core functionality of Article objects including:
 * - Object creation and field initialization
 * - Getter method functionality
 * - String representation
 */
@DisplayName("Article Class Tests")
class ArticleTest {
    private Article article;
    private Source source;

    @BeforeEach
    void setUp() {
        source = new Source("test-id", "Test Source");
        // Create a fully populated article for testing
        article = new Article(
                "Test Title",
                "Test Description",
                "2024-01-01",
                "https://test.com",
                "https://test.com/image",
                "Test content",
                source,
                "Test Author"
        );
    }

    @Nested
    @DisplayName("Constructor and Getters")
    class ConstructorAndGettersTests {
        /**
         * Verifies that all fields are correctly initialized during article creation
         * and accessible through their respective getter methods.
         */
        @Test
        @DisplayName("All fields are correctly initialized and accessible")
        void testArticleCreation() {
            assertAll("Article properties",
                    () -> assertEquals("Test Title", article.getTitle(), "Title should match"),
                    () -> assertEquals("Test Description", article.getDescription(), "Description should match"),
                    () -> assertEquals("2024-01-01", article.getPublishedAt(), "Published date should match"),
                    () -> assertEquals("https://test.com", article.getUrl(), "URL should match"),
                    () -> assertEquals("https://test.com/image", article.getUrlToImage(), "Image URL should match"),
                    () -> assertEquals("Test content", article.getContent(), "Content should match"),
                    () -> assertEquals(source, article.getSource(), "Source should match"),
                    () -> assertEquals("Test Author", article.getAuthor(), "Author should match")
            );
        }
    }

    /**
     * Test suite for the Source class.
     * Verifies the core functionality of Source objects including:
     * - Object creation and field initialization
     * - Getter method functionality
     */

    @Nested
    @DisplayName("Source Class Tests")
    class SourceTest {
        private Source source;

        @BeforeEach
        void setUp() {
            source = new Source("test-id", "Test Source");
        }

        @Test
        @DisplayName("getId returns the correct ID")
        void testGetId() {
            assertEquals("test-id", source.getId(), "getId should return the correct ID");
        }

        @Test
        @DisplayName("getName returns the correct name")
        void testGetName() {
            assertEquals("Test Source", source.getName(), "getName should return the correct name");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {
        /**
         * Verifies that toString() produces the expected formatted output
         * including title, date, URL, and description.
         */
        @Test
        @DisplayName("toString provides correct format")
        void testToString() {
            String expected = String.format(
                    "title: Test Title%n" +
                            "at: 2024-01-01%n" +
                            "url: https://test.com%n" +
                            "Test Description%n"
            );
            assertEquals(expected, article.toString(), "toString should match expected format");
        }

        /**
         * Verifies toString handles null fields gracefully
         */
        @Test
        @DisplayName("toString handles null fields")
        void testToStringWithNullFields() {
            Article nullArticle = new Article(
                    null, null, null, null, null, null, null, null
            );
            String expected = String.format(
                    "title: N/A%n" +
                            "at: N/A%n" +
                            "url: N/A%n" +
                            "N/A%n"
            );
            assertEquals(expected, nullArticle.toString(), "toString should handle null fields");
        }
    }
}

