package org.article.core;

/**
 * Interface for different article printing strategies.
 */
public interface ArticlePrinter {
    /**
     * Prints article information in a specific format.
     * @param article the article to print
     */
    void print(Article article);
}
