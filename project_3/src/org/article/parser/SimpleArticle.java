package org.article.parser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an article in the simple format.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleArticle {
    private final String title;
    private final String description;
    private final String publishedAt;
    private final String url;

    /**
     * Constructs a SimpleArticle with the provided fields.
     *
     * @param title the title of the article
     * @param description the description of the article
     * @param publishedAt the date the article was published
     * @param url the URL of the article
     */
    @JsonCreator
    public SimpleArticle(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("publishedAt") String publishedAt,
            @JsonProperty("url") String url) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    /**
     * Returns the title, Description, Date and Url  of the article.
     *
     * @return the title, Description, Date and Url  of the article
     */
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return publishedAt; }
    public String getUrl() { return url; }
}