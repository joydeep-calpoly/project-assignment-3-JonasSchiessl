package org.article.core;

import java.io.IOException;

/**
 * Represents a source of article data.
 */
public interface DataSource {
    /**
     * Gets the raw data from the source.
     * @return Raw data as string
     * @throws IOException if data cannot be retrieved
     */
    String getData() throws IOException;
}
