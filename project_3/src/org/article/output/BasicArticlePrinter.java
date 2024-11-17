package org.article.output;

import org.article.core.Article;
import org.article.core.ArticlePrinter;

/**
 * Prints basic article information to console.
 */
public class BasicArticlePrinter implements ArticlePrinter {
    /**
     * Prints article information in basic format.
     * @param article the article to print
     */
    @Override
    public void print(Article article) {
        ArticleFormatter formatter = new ArticleFormatter(article);
        System.out.print(formatter.formatBasic());
        System.out.println(); // Add blank line between articles
    }
}
