package org.article.util;

import org.article.core.Article;

/**
 * Interface for article validation.
 */
public interface ArticleValidator {
    /**
     * Validates the provided article.
     *
     * @param article the article to validate
     * @return true if the article is valid, false otherwise
     */
    boolean isValid(Article article);
}