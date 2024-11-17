package org.article.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.article.core.Article;
import java.util.List;

/**
 * Represents a response containing a list of articles from a news API.
 * The {@code ArticleResponse} class includes the status of the response,
 * the total number of results, and a list of articles.
 * <p>
 * It uses Jackson annotations to map JSON fields to Java fields.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("totalResults")
    private int totalResults;

    @JsonProperty("articles")
    private List<Article> articles;


    /**
     * Returns the status of the response.
     *
     * @return the status of the response.
     */
    @SuppressWarnings("unused")
    public String getStatus() {
        return status;
    }

    /**
     * Returns the total number of results in the response.
     *
     * @return the total number of results.
     */
    @SuppressWarnings("unused")
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * Returns the list of articles in the response.
     *
     * @return the list of articles.
     */
    public List<Article> getArticles() {
        return articles;
    }
}