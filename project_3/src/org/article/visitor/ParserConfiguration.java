package org.article.visitor;
import org.article.core.Parser;

/**
 * Represents a parser configuration that can be visited.
 * Part of the Visitor pattern implementation.
 */
public interface ParserConfiguration {
    /**
     * Accepts a parser visitor to create the appropriate parser.
     * @param visitor The visitor to accept
     * @return A configured parser instance
     */
    Parser accept(ParserVisitor visitor);
}