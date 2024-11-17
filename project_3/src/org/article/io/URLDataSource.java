package org.article.io;


import org.article.core.DataSource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of DataSource that retrieves data from a URL.
 */
public class URLDataSource implements DataSource {
    private final String url;
    private final HttpClient httpClient;

    /**
     * Constructs a URL data source with the provided URL.
     *
     * @param url the URL to fetch data from
     */
    public URLDataSource(String url) {
        this(url, HttpClient.newHttpClient());
    }

    /**
     * Constructs a URL data source with the provided URL and HTTP client.
     *
     * @param url the URL to fetch data from
     * @param httpClient the HTTP client to use for fetching data
     */
    public URLDataSource(String url, HttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    /**
     * Gets the raw data from the URL.
     * @return Raw data as string
     * @throws IOException if data cannot be retrieved
     */
    @Override
    public String getData() throws IOException {
        try {
            URI uri = createURI(url);
            return fetchData(uri);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid URL format: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    // Helper methods
    private URI createURI(String url) throws IOException {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL format: " + url, e);
        }
    }
    // Helper methods
    private String fetchData(URI uri) throws IOException, InterruptedException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            return body != null ? body : "";
        } catch (IOException e) {
            throw new IOException("Error fetching data from URL: " + uri, e);
        }
    }
}
