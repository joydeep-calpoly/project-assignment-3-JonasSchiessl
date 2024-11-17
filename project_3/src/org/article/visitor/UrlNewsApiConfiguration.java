package org.article.visitor;

import org.article.core.Parser;

/**
 * Configuration for URL-based NewsAPI format parsing.
 */
public class UrlNewsApiConfiguration implements ParserConfiguration {
    private final String url;

    /**
     * Constructs a new URL-based NewsAPI configuration.
     * @param url The URL to parse from
     */
    public UrlNewsApiConfiguration(String url) {
        this.url = url;
    }

    /**
     * Gets the URL for this configuration.
     * @return The configured URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Accepts a visitor to create a parser instance.
     * @param visitor The visitor to accept
     * @return The created parser instance
     */
    @Override
    public Parser accept(ParserVisitor visitor) {
        return visitor.visitUrlNewsApi(this);
    }
}