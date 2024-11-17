package org.article.visitor;

import org.article.core.Parser;

/**
 * Configuration for file-based Simple format parsing.
 */
public class FileSimpleConfiguration implements ParserConfiguration {
    private final String filePath;

    /**
     * Constructs a new file-based Simple format configuration.
     * @param filePath The path to the file to parse
     */
    public FileSimpleConfiguration(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the file path for this configuration.
     * @return The configured file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Accepts a visitor to create a parser instance.
     * @param visitor The visitor to accept
     * @return The created parser instance
     */
    @Override
    public Parser accept(ParserVisitor visitor) {
        return visitor.visitFileSimple(this);
    }
}