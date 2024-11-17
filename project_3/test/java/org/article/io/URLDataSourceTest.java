package org.article.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test suite for URLDataSource.
 * Tests URL data retrieval and error handling.
 */
@DisplayName("URLDataSource Tests")
class URLDataSourceTest {
    @Mock private HttpClient mockHttpClient;
    @Mock private HttpResponse<String> mockResponse;

    private URLDataSource urlDataSource;
    private static final String TEST_URL = "https://api.example.com/data";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        urlDataSource = new URLDataSource(TEST_URL, mockHttpClient);
    }

    /**
     * Tests successful data retrieval from URL.
     */
    @Test
    @DisplayName("Successfully retrieves data from URL")
    void testSuccessfulDataRetrieval() throws Exception {
        String expectedData = "{\"key\":\"value\"}";

        when(mockResponse.body()).thenReturn(expectedData);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = urlDataSource.getData();
        assertAll(
                () -> assertEquals(expectedData, result, "Retrieved data should match expected"),
                () -> verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)),
                () -> verify(mockResponse).body()
        );
    }

    /**
     * Tests handling of invalid URL formats.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-url",
            "not_a_url",
            "https://",
            "ftp:",
            " "
    })
    @DisplayName("Handles invalid URL formats")
    void testInvalidURLFormats(String invalidUrl) {
        URLDataSource invalidDataSource = new URLDataSource(invalidUrl);

        IOException exception = assertThrows(IOException.class,
                invalidDataSource::getData,
                "Should throw IOException for invalid URL"
        );
        assertTrue(exception.getMessage().contains("Invalid URL format"),
                "Exception message should indicate invalid URL format");
    }

    /**
     * Tests handling of null response body.
     */
    @Test
    @DisplayName("Handles null response body")
    void testNullResponseBody() throws Exception {
        when(mockResponse.body()).thenReturn(null);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = urlDataSource.getData();
        assertAll(
                () -> assertNotNull(result, "Result should not be null even with null response"),
                () -> assertTrue(result.isEmpty(), "Result should be empty string for null response"),
                () -> verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))
        );
    }

    /**
     * Tests handling of network errors.
     */
    @Test
    @DisplayName("Handles network errors")
    void testNetworkError() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));
        IOException exception = assertThrows(IOException.class,
                () -> urlDataSource.getData(),
                "Should throw IOException on network error"
        );
        assertTrue(exception.getMessage().contains("Error fetching data from URL"),
                "Exception message should indicate URL fetch error");
    }

    /**
     * Tests handling of request interruption.
     */
    @Test
    @DisplayName("Handles request interruption")
    void testInterruption() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Request interrupted"));
        IOException exception = assertThrows(IOException.class,
                () -> urlDataSource.getData(),
                "Should wrap InterruptedException in IOException"
        );
        assertAll(
                () -> assertTrue(exception.getMessage().contains("Request interrupted"),
                        "Exception message should indicate interruption"),
                () -> assertTrue(Thread.currentThread().isInterrupted(),
                        "Thread should be marked as interrupted")
        );
    }
}
