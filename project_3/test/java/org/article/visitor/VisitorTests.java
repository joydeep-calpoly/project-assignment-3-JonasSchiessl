package org.article.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.article.core.Article;
import org.article.core.DataSource;
import org.article.core.Parser;
import org.article.core.Source;
import org.article.io.FileDataSource;
import org.article.io.URLDataSource;
import org.article.parser.NewsApiParser;
import org.article.parser.NewsApiResponse;
import org.article.parser.ParserException;
import org.article.parser.SimpleParser;
import org.article.util.ArticleLogger;
import org.article.util.ArticleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test suite for the Visitor pattern implementation.
 * Tests creation and configuration of parsers using the visitor pattern.
 */
@DisplayName("Visitor Pattern Tests")
class VisitorTests {
    @Mock private ObjectMapper mockMapper;
    @Mock private ArticleValidator mockValidator;
    @Mock private ArticleLogger mockLogger;
    private ConcreteParserVisitor visitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        visitor = new ConcreteParserVisitor(mockMapper, mockValidator, mockLogger);
    }

    /**
     * Tests for the configuration factory.
     */
    @Nested
    @DisplayName("Configuration Factory Tests")
    class ConfigurationFactoryTests {

        /**
         * Tests creation of file-based configurations for both NewsAPI and Simple formats.
         */
        @Test
        @DisplayName("Creates file-based configurations")
        void testFileConfigurations() {
            String filePath = "test.json";
            ParserConfiguration newsApiConfig = ParserConfigurationFactory.createConfiguration(
                    "file", "newsapi", filePath);
            ParserConfiguration simpleConfig = ParserConfigurationFactory.createConfiguration(
                    "file", "simple", filePath);

            assertAll(
                    () -> assertInstanceOf(FileNewsApiConfiguration.class, newsApiConfig),
                    () -> assertInstanceOf(FileSimpleConfiguration.class, simpleConfig),
                    () -> {
                        assert newsApiConfig instanceof FileNewsApiConfiguration;
                        assertEquals(filePath, ((FileNewsApiConfiguration) newsApiConfig).getFilePath());
                    },
                    () -> {
                        assert simpleConfig instanceof FileSimpleConfiguration;
                        assertEquals(filePath, ((FileSimpleConfiguration) simpleConfig).getFilePath());
                    }
            );
        }

        /**
         * Tests creation of URL-based configuration for NewsAPI format.
         */
        @Test
        @DisplayName("Creates URL-based configuration")
        void testUrlConfiguration() {
            String url = "https://test.com/api";
            ParserConfiguration config = ParserConfigurationFactory.createConfiguration(
                    "url", "newsapi", url);

            assertAll(
                    () -> assertInstanceOf(UrlNewsApiConfiguration.class, config),
                    () -> {
                        assert config instanceof UrlNewsApiConfiguration;
                        assertEquals(url, ((UrlNewsApiConfiguration) config).getUrl());
                    }
            );
        }

        /**
         * Tests error handling for invalid source types.
         */
        @ParameterizedTest
        @ValueSource(strings = {"ftp", "database", "invalid"})
        @DisplayName("Handles invalid source types")
        void testInvalidSourceType(String sourceType) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> ParserConfigurationFactory.createConfiguration(sourceType, "newsapi", "test")
            );
            assertTrue(exception.getMessage().contains("Invalid source type"));
        }

        /**
         * Tests error handling for invalid format types.
         */
        @Test
        @DisplayName("Handles invalid format type")
        void testInvalidFormatType() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> ParserConfigurationFactory.createConfiguration("file", "invalid", "test")
            );
            assertTrue(exception.getMessage().contains("Invalid format"));
        }
    }

    /**
     * Tests for the visitor pattern implementation.
     */
    @Nested
    @DisplayName("Visitor Implementation Tests")
    class VisitorImplementationTests {

        /**
         * Tests creation of FileNewsApiParser.
         */
        @Test
        @DisplayName("Creates FileNewsApiParser")
        void testFileNewsApiParser() {
            FileNewsApiConfiguration config = new FileNewsApiConfiguration("test.json");
            Parser parser = visitor.visitFileNewsApi(config);

            assertAll(
                    () -> assertInstanceOf(NewsApiParser.class, parser),
                    () -> assertInstanceOf(FileDataSource.class, parser.getDataSource())
            );
        }

        /**
         * Tests creation of FileSimpleParser.
         */
        @Test
        @DisplayName("Creates FileSimpleParser")
        void testFileSimpleParser() {
            FileSimpleConfiguration config = new FileSimpleConfiguration("test.json");
            Parser parser = visitor.visitFileSimple(config);

            assertAll(
                    () -> assertInstanceOf(SimpleParser.class, parser),
                    () -> assertInstanceOf(FileDataSource.class, parser.getDataSource())
            );
        }

        /**
         * Tests creation of UrlNewsApiParser.
         */
        @Test
        @DisplayName("Creates UrlNewsApiParser")
        void testUrlNewsApiParser() {
            UrlNewsApiConfiguration config = new UrlNewsApiConfiguration("https://test.com/api");
            Parser parser = visitor.visitUrlNewsApi(config);

            assertAll(
                    () -> assertInstanceOf(NewsApiParser.class, parser),
                    () -> assertInstanceOf(URLDataSource.class, parser.getDataSource())
            );
        }
    }

    /**
     * Integration tests for the visitor pattern.
     */
    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {
        private static final String ARTICLES_FIELD = "articles";
        @Mock private DataSource mockDataSource;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        /**
         * Tests creation of FileNewsApiParser with complete flow.
         */
        @Test
        @DisplayName("Tests complete parser creation flow")
        void testCompleteFlow() throws Exception {
            String jsonData = """
            {
                "status": "ok",
                "articles": [
                    {
                        "title": "Test Title",
                        "description": "Test Description",
                        "publishedAt": "2024-01-01",
                        "url": "https://test.com",
                        "source": {
                            "id": "test-source",
                            "name": "Test Source"
                        }
                    }
                ]
            }
            """;

            ParserConfiguration config = new FileNewsApiConfiguration("test.json") {
                @Override
                public Parser accept(ParserVisitor visitor) {
                    return new NewsApiParser(mockMapper, mockValidator, mockLogger) {
                        @Override
                        public DataSource getDataSource() {
                            return mockDataSource;
                        }
                    };
                }
            };

            when(mockDataSource.getData()).thenReturn(jsonData);
            when(mockValidator.isValid(any())).thenReturn(true);
            NewsApiResponse mockResponse = new NewsApiResponse();
            List<Article> mockArticles = List.of(new Article(
                    "Test Title",
                    "Test Description",
                    "2024-01-01",
                    "https://test.com",
                    null,
                    null,
                    new Source("test-source", "Test Source"),
                    null
            ));
            setPrivateField(mockResponse, mockArticles);
            when(mockMapper.readValue(anyString(), eq(NewsApiResponse.class)))
                    .thenReturn(mockResponse);
            Parser parser = config.accept(visitor);
            List<Article> articles = parser.parse(mockDataSource);

            assertAll(
                    () -> assertNotNull(parser),
                    () -> assertInstanceOf(NewsApiParser.class, parser),
                    () -> assertNotNull(articles),
                    () -> assertFalse(articles.isEmpty()),
                    () -> assertEquals("Test Title", articles.getFirst().getTitle())
            );
        }

        /**
         * Tests error handling in complete flow.
         */
        @Test
        @DisplayName("Handles errors in complete flow")
        void testErrorHandling() throws Exception {
            // Create configuration with mocked data source
            ParserConfiguration config = new FileNewsApiConfiguration("test.json") {
                @Override
                public Parser accept(ParserVisitor visitor) {
                    return new NewsApiParser(mockMapper, mockValidator, mockLogger) {
                        @Override
                        public DataSource getDataSource() {
                            return mockDataSource;
                        }
                    };
                }
            };

            when(mockDataSource.getData())
                    .thenThrow(new RuntimeException("Test error"));
            Parser parser = config.accept(visitor);

            assertThrows(ParserException.class, () -> parser.parse(mockDataSource));
            verify(mockLogger).error(anyString(), any(Exception.class));
        }

        /**
         * Tests error handling for unsupported format with URL source type.
         */
        @Test
        @DisplayName("Handles unsupported format for URL source type")
        void testUrlUnsupportedFormat() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> ParserConfigurationFactory.createConfiguration("url", "simple", "https://example.com")
            );
            assertTrue(exception.getMessage().contains("URL source only supports NewsAPI format"));
        }

        private void setPrivateField(NewsApiResponse target, Object value)
                throws Exception {
            Field field = target.getClass().getDeclaredField(IntegrationTests.ARTICLES_FIELD);
            field.setAccessible(true);
            field.set(target, value);
        }
    }
}