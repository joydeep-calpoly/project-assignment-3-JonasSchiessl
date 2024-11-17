package org.article.core;

import org.article.parser.ParserException;
import java.util.List;

/**
 * Core interface for all article parsers.
 */
public interface Parser {
    /**
     * Parses data and returns a list of articles.
     * @param source The data source to parse
     * @return List of parsed articles
     * @throws ParserException if parsing fails
     */
    List<Article> parse(DataSource source) throws ParserException;

    /**
     * Gets the data source associated with this parser.
     * @return The data source for this parser
     */
    DataSource getDataSource();
}
