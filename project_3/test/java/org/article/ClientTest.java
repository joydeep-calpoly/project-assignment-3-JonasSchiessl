package org.article;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.article.core.Article;
import org.article.output.BasicArticlePrinter;
import org.article.core.ArticlePrinter;
import org.article.util.*;
import org.article.visitor.ConcreteParserVisitor;
import org.article.visitor.ParserVisitor;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test suite for Client class.
 * Tests command line argument handling, data source creation, and error handling.
 */
@DisplayName("Client Tests")
class ClientTest {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private ArticlePrinter printer;
    private ArticleLogger logger;
    private Client client;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException, LoggingException {
        tempDir = Files.createTempDirectory("test");
        printer = spy(new BasicArticlePrinter());
        ArticleValidator validator = spy(new RequiredFieldsValidator());
        logger = spy(new FileArticleLogger(tempDir.resolve("test.log").toString()));
        ParserVisitor visitor = new ConcreteParserVisitor(objectMapper, validator, logger);
        client = new Client(printer, visitor);
    }

    @AfterEach
    void tearDown() throws IOException {
        try (Stream<Path> pathStream = Files.walk(tempDir)) {
            pathStream.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.warning("Failed to delete test file: " + path);
                        }
                    });
        }
    }

    /**
     * Test cases for command line argument handling.
     */
    @Test
    @DisplayName("Should process valid NewsAPI file")
    void shouldProcessValidNewsApiFile() throws Exception {
        String json = """
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

        Path testFile = tempDir.resolve("test.json");
        Files.writeString(testFile, json);
        client.run(new String[]{"file", testFile.toString(), "newsapi"});
        verify(printer, times(1)).print(any(Article.class));
    }

    /**
     * Test cases for invalid file path.
     */
    @Test
    @DisplayName("Should handle invalid file path")
    void shouldHandleInvalidFilePath() {
        assertThrows(ClientException.class, () ->
                client.run(new String[]{"file", "nonexistent.json", "newsapi"})
        );
    }

    /**
     * Test cases for invalid JSON format.
     */
    @Test
    @DisplayName("Should handle invalid format specification")
    void shouldHandleInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                client.run(new String[]{"file", "test.json", "invalid"})
        );
    }

    /**
     * Test cases for invalid JSON format.
     */
    @Test
    @DisplayName("Should process valid Simple format file")
    void shouldProcessValidSimpleFile() throws Exception {
        // Prepare test data
        String json = """
        {
            "title": "Test Title",
            "description": "Test Description",
            "publishedAt": "2024-01-01",
            "url": "https://test.com"
        }
        """;

        Path tempDir = Files.createTempDirectory("testDir");
        Path testFile = tempDir.resolve("simple.json");
        Files.writeString(testFile, json);
        doNothing().when(printer).print(any(Article.class));
        client.run(new String[]{"file", testFile.toString(), "simple"});
        verify(printer, times(1)).print(any(Article.class));
        Files.deleteIfExists(testFile);
        Files.deleteIfExists(tempDir);
    }

    /**
     * Test cases for invalid JSON format.
     */
    @Test
    @DisplayName("Should handle URL source type")
    void shouldHandleUrlSourceType() {
        assertThrows(ClientException.class, () ->
                client.run(new String[]{"url", "https://invalid.url", "newsapi"})
        );
    }

    /**
     * Test cases for invalid JSON format.
     */
    @Test
    @DisplayName("Should handle missing arguments")
    void shouldHandleMissingArguments() {
        assertThrows(IllegalArgumentException.class, () ->
                client.run(new String[]{"file"})
        );
    }

    /**
     * Test cases for invalid JSON format.
     */
    @Test
    @DisplayName("Should handle empty file")
    void shouldHandleEmptyFile() throws Exception {
        Path testFile = tempDir.resolve("empty.json");
        Files.writeString(testFile, "");

        assertThrows(ClientException.class, () ->
                client.run(new String[]{"file", testFile.toString(), "newsapi"})
        );
    }
}