package org.article.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.article.output.ArticleFormatter;


/**
 * Represents an article from a news source.
 * Contains the article's title, description, publication date, URL, image URL, content, source, and author.
 */
public class Article {
    private final String title;
    private final String description;
    private final String publishedAt;
    private final String url;
    private final String urlToImage;
    private final String content;
    private final Source source;
    private final String author;

    /**
     * Creates an Article instance with the specified attributes.
     * Uses Jackson annotations for JSON deserialization.
     */
    @JsonCreator
    public Article(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("publishedAt") String publishedAt,
            @JsonProperty("url") String url,
            @JsonProperty("urlToImage") String urlToImage,
            @JsonProperty("content") String content,
            @JsonProperty("source") Source source,
            @JsonProperty("author") String author
    ) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
        this.urlToImage = urlToImage;
        this.content = content;
        this.source = source;
        this.author = author;
    }

    /**
     * Returns the article's title, description, publication date, URL, image URL, content, source, and author.
     */
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPublishedAt() { return publishedAt; }
    public String getUrl() { return url; }
    public String getUrlToImage() { return urlToImage; }
    public String getContent() { return content; }
    public Source getSource() { return source; }
    public String getAuthor() { return author; }

    /**
     * Creates a formatted string representation of the article's data.
     */
    @Override
    public String toString() {
        return new ArticleFormatter(this).formatBasic();
    }

}

