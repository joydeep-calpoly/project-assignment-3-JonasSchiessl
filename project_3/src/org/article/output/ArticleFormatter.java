package org.article.output;

import org.article.core.Article;

/**
 * Helper class for formatting Article data.
 * Separated to keep formatting logic out of the main Article class.
 */
public class ArticleFormatter {
    private final Article article;

    /**
     * Constructs an ArticleFormatter with the provided article.
     * @param article the article to format
     */
    public ArticleFormatter(Article article) {
        this.article = article;
    }

    /**
     * Formats article data in basic format.
     * @return formatted string with basic article information
     */
    public String formatBasic() {
        return String.format(
                "title: %s%n" +
                        "at: %s%n" +
                        "url: %s%n" +
                        "%s%n",
                formatOrDefault(article.getTitle()),
                formatOrDefault(article.getPublishedAt()),
                formatOrDefault(article.getUrl()),
                formatOrDefault(article.getDescription())
        );
    }


    private String formatOrDefault(String value) {
        return value != null ? value : "N/A";
    }
}
