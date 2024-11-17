package org.article.util;

import org.article.core.Article;
import org.article.core.Source;

/**
 * Validates required fields in an article.
 */
public class RequiredFieldsValidator implements ArticleValidator {
    /**
     * Validates that the article has all required fields.
     *
     * @param article the article to validate
     * @return true if the article is valid, false otherwise
     */
    @Override
    public boolean isValid(Article article) {
        return article != null &&
                isNotEmpty(article.getTitle()) &&
                isNotEmpty(article.getDescription()) &&
                isNotEmpty(article.getPublishedAt()) &&
                isNotEmpty(article.getUrl()) &&
                hasValidSource(article.getSource());
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean hasValidSource(Source source) {
        return source != null && isNotEmpty(source.getName());
    }
}