package org.article.visitor;

/**
 * Factory for creating parser configurations based on source type and format.
 */
public class ParserConfigurationFactory {
    /**
     * Creates a parser configuration based on source type and format.
     * @param sourceType The type of source ("file" or "url")
     * @param format The format type ("newsapi" or "simple")
     * @param pathOrUrl The path or URL to the source
     * @return A parser configuration matching the specified parameters
     * @throws IllegalArgumentException if invalid combination of parameters
     */
    public static ParserConfiguration createConfiguration(String sourceType, String format, String pathOrUrl) {
        return switch (sourceType.toLowerCase()) {
            case "file" -> switch (format.toLowerCase()) {
                case "newsapi" -> new FileNewsApiConfiguration(pathOrUrl);
                case "simple" -> new FileSimpleConfiguration(pathOrUrl);
                default -> throw new IllegalArgumentException("Invalid format: " + format);
            };
            case "url" -> {
                if (!format.equalsIgnoreCase("newsapi")) {
                    throw new IllegalArgumentException("URL source only supports NewsAPI format");
                }
                yield new UrlNewsApiConfiguration(pathOrUrl);
            }
            default -> throw new IllegalArgumentException("Invalid source type: " + sourceType);
        };
    }
}