package org.article.visitor;

import org.article.core.Parser;

/**
 * Configuration for file-based NewsAPI format parsing.
 */
public class FileNewsApiConfiguration implements ParserConfiguration {
    private final String filePath;

    /**
     * Constructs a new file-based NewsAPI configuration.
     * @param filePath The path to the file to parse
     */
    public FileNewsApiConfiguration(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the file path for this configuration.
     * @return The configured file path
     */
    public String getFilePath() {
        return filePath;
    }

    @Override
    public Parser accept(ParserVisitor visitor) {
        return visitor.visitFileNewsApi(this);
    }
}