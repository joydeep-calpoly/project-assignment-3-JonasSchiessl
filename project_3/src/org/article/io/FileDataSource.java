package org.article.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.article.core.DataSource;

/**
 * Represents a file data source.
 */
public class FileDataSource implements DataSource {
    private final String filePath;
    /**
     * Constructs a file data source with the provided file path.
     *
     * @param filePath the path to the file
     */
    public FileDataSource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the raw data from the file.
     * @return Raw data as string
     * @throws IOException if data cannot be retrieved
     */
    @Override
    public String getData() throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
