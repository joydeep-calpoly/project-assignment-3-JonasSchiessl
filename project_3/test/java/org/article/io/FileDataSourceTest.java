package org.article.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for FileDataSource.
 * Verifies file reading operations and error handling.
 */
@DisplayName("FileDataSource Tests")
class FileDataSourceTest {
    private static final String TEST_CONTENT = "test content";
    private Path tempFile;
    private FileDataSource dataSource;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, TEST_CONTENT.getBytes());
        dataSource = new FileDataSource(tempFile.toString());
    }

    /**
     * Tests successful reading of file content.
     * Verifies that file content is read correctly.
     */
    @Test
    @DisplayName("Successfully reads file content")
    void testGetData() throws IOException {
        String content = dataSource.getData();
        assertEquals(TEST_CONTENT, content, "File content should match test content");
    }

    /**
     * Tests error handling for non-existent files.
     * Verifies appropriate exception throwing.
     */
    @Test
    @DisplayName("Handles non-existent file appropriately")
    void testGetDataNonExistentFile() {
        FileDataSource invalidSource = new FileDataSource("nonexistent.json");
        assertThrows(IOException.class, invalidSource::getData,
                "Should throw IOException for non-existent file");
    }

    /**
     * Cleanup test files after each test
     */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
