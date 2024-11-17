package org.article.util;

import org.article.core.Article;
import org.article.core.Source;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for RequiredFieldsValidator.
 * Verifies validation logic for required article fields including:
 * - Title, Description, PublishedAt, URL, and Source validation
 * - Handling of null and empty values
 * - Edge cases
 * - Valid article configurations
 */
@DisplayName("RequiredFieldsValidator Tests")
class RequiredFieldsValidatorTest {
    private RequiredFieldsValidator validator;
    private Source validSource;

    @BeforeEach
    void setUp() {
        validator = new RequiredFieldsValidator();
        validSource = new Source("test-id", "Test Source");
    }

    /**
     * Tests validation of a fully populated valid article.
     */
    @Nested
    @DisplayName("Valid Article Tests")
    class ValidArticleTests {

        /**
         * Tests validation of an article with all required fields.
         */
        @Test
        @DisplayName("Validates article with all required fields")
        void testValidArticle() {
            Article article = new Article(
                    "Test Title",
                    "Test Description",
                    "2024-01-01",
                    "https://test.com",
                    null,  // Optional field
                    null,  // Optional field
                    validSource,
                    null   // Optional field
            );

            assertTrue(validator.isValid(article),
                    "Article with all required fields should be valid");
        }
    }

    /**
     * Tests edge cases for full coverage of validation logic.
     */
    @Nested
    @DisplayName("Edge Case Tests for Full Coverage")
    class EdgeCaseTests {
        /**
         * Tests validation of an article with a null source ID.
         */
        @Test
        @DisplayName("Null article is invalid")
        void testNullArticle() {
            assertFalse(validator.isValid(null), "Null article should be invalid");
        }

        /**
         * Tests validation of an article with a null source ID.
         */
        @Test
        @DisplayName("Null title is invalid")
        void testNullTitle() {
            Article article = new Article(
                    null, "Valid Description", "2024-01-01", "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with null title should be invalid");
        }

        /**
         * Tests validation of an article with an empty title.
         */
        @Test
        @DisplayName("Empty title is invalid")
        void testEmptyTitle() {
            Article article = new Article(
                    "", "Valid Description", "2024-01-01", "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with empty title should be invalid");
        }

        /**
         * Tests validation of an article with a null description.
         */
        @Test
        @DisplayName("Null description is invalid")
        void testNullDescription() {
            Article article = new Article(
                    "Valid Title", null, "2024-01-01", "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with null description should be invalid");
        }

        /**
         * Tests validation of an article with an empty description.
         */
        @Test
        @DisplayName("Empty description is invalid")
        void testEmptyDescription() {
            Article article = new Article(
                    "Valid Title", "", "2024-01-01", "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with empty description should be invalid");
        }

        /**
         * Tests validation of an article with a null publishedAt date.
         */
        @Test
        @DisplayName("Null publishedAt is invalid")
        void testNullPublishedAt() {
            Article article = new Article(
                    "Valid Title", "Valid Description", null, "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with null publishedAt should be invalid");
        }

        /**
         * Tests validation of an article with an empty publishedAt date.
         */
        @Test
        @DisplayName("Empty publishedAt is invalid")
        void testEmptyPublishedAt() {
            Article article = new Article(
                    "Valid Title", "Valid Description", "", "https://test.com", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with empty publishedAt should be invalid");
        }

        /**
         * Tests validation of an article with a null URL.
         */
        @Test
        @DisplayName("Null URL is invalid")
        void testNullUrl() {
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", null, null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with null URL should be invalid");
        }

        /**
         * Tests validation of an article with an empty URL.
         */
        @Test
        @DisplayName("Empty URL is invalid")
        void testEmptyUrl() {
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "", null, null, validSource, null);
            assertFalse(validator.isValid(article), "Article with empty URL should be invalid");
        }

        /**
         * Tests validation of an article with a null source.
         */
        @Test
        @DisplayName("Null source is invalid")
        void testNullSource() {
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "https://test.com", null, null, null, null);
            assertFalse(validator.isValid(article), "Article with null source should be invalid");
        }

        /**
         * Tests validation of an article with a null source ID.
         */
        @Test
        @DisplayName("Source with null ID is valid")
        void testSourceWithNullId() {
            Source source = new Source(null, "Valid Name");
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "https://test.com", null, null, source, null);
            assertTrue(validator.isValid(article), "Article with source having null ID should be valid");
        }

        /**
         * Tests validation of an article with a source with an empty ID.
         */
        @Test
        @DisplayName("Source with empty ID is valid")
        void testSourceWithEmptyId() {
            Source source = new Source("", "Valid Name");
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "https://test.com", null, null, source, null);
            assertTrue(validator.isValid(article), "Article with source having empty ID should be valid");
        }

        /**
         * Tests validation of an article with a source with a null name.
         */
        @Test
        @DisplayName("Source with null name is invalid")
        void testSourceWithNullName() {
            Source invalidSource = new Source("Valid ID", null);
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "https://test.com", null, null, invalidSource, null);
            assertFalse(validator.isValid(article), "Article with source having null name should be invalid");
        }

        /**
         * Tests validation of an article with a source with an empty name.
         */
        @Test
        @DisplayName("Source with empty name is invalid")
        void testSourceWithEmptyName() {
            Source invalidSource = new Source("Valid ID", "");
            Article article = new Article(
                    "Valid Title", "Valid Description", "2024-01-01", "https://test.com", null, null, invalidSource, null);
            assertFalse(validator.isValid(article), "Article with source having empty name should be invalid");
        }
    }

    /**
     * Tests validation of an article with a null source.
     */
    @Nested
    @DisplayName("Invalid Article Tests")
    class InvalidArticleTests {

        /**
         * Tests validation with various invalid article configurations.
         * @param article The article to test
         */
        @ParameterizedTest(name = "Invalid case #{index}")
        @MethodSource("invalidArticleProvider")
        @DisplayName("Detects invalid articles")
        void testInvalidArticles(Article article) {
            assertFalse(validator.isValid(article),
                    "Article with missing required fields should be invalid");
        }

        /**
         * Provides test cases for invalid articles.
         * @return Stream of test cases with invalid articles
         */
        private static Stream<Arguments> invalidArticleProvider() {
            Source validSource = new Source("id", "name");
            return Stream.of(
                    Arguments.of(Named.of("Null title",
                            new Article(null, "desc", "date", "url", null, null, validSource, null))),
                    Arguments.of(Named.of("Empty title",
                            new Article("", "desc", "date", "url", null, null, validSource, null))),
                    Arguments.of(Named.of("Null description",
                            new Article("title", null, "date", "url", null, null, validSource, null))),
                    Arguments.of(Named.of("Null date",
                            new Article("title", "desc", null, "url", null, null, validSource, null))),
                    Arguments.of(Named.of("Null URL",
                            new Article("title", "desc", "date", null, null, null, validSource, null))),
                    Arguments.of(Named.of("Null source",
                            new Article("title", "desc", "date", "url", null, null, null, null)))
            );
        }
    }
}
