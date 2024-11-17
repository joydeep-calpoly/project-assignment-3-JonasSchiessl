package org.article.visitor;
import org.article.core.Parser;

/**
 * Visitor interface for creating parsers based on source and format.
 * Implements the Visitor pattern to allow dynamic parser construction.
 */
public interface ParserVisitor {
    /**
     * Visits a file source with NewsAPI format configuration.
     * @param config The parser configuration to visit
     * @return A parser configured for file-based NewsAPI parsing
     */
    Parser visitFileNewsApi(ParserConfiguration config);

    /**
     * Visits a file source with Simple format configuration.
     * @param config The parser configuration to visit
     * @return A parser configured for file-based Simple format parsing
     */
    Parser visitFileSimple(ParserConfiguration config);

    /**
     * Visits a URL source with NewsAPI format configuration.
     * @param config The parser configuration to visit
     * @return A parser configured for URL-based NewsAPI parsing
     */
    Parser visitUrlNewsApi(ParserConfiguration config);
}