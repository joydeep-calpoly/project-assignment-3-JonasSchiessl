package org.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.article.output.BasicArticlePrinter;
import org.article.parser.ParserException;
import org.article.util.*;
import org.article.core.Article;
import org.article.core.Parser;
import org.article.core.ArticlePrinter;
import org.article.visitor.ConcreteParserVisitor;
import org.article.visitor.ParserConfiguration;
import org.article.visitor.ParserConfigurationFactory;
import org.article.visitor.ParserVisitor;
import java.util.List;

/**
 * Client for parsing and printing articles from different sources.
 * Uses Visitor pattern to determine appropriate parser configuration.
 */
public class Client {
    private final ArticlePrinter printer;
    private final ParserVisitor visitor;

    /**
     * Constructs a client with the provided dependencies.
     * @param printer The printer to use for output
     * @param visitor The visitor for creating parsers
     */
    Client(ArticlePrinter printer, ParserVisitor visitor) {
        this.printer = printer;
        this.visitor = visitor;
    }

    /**
     * Displays usage instructions for the client.
     */
    private static void printUsage() {
        System.out.println("Usage: java Client <source_type> <path_or_url> [format]");
        System.out.println("source_type: file or url");
        System.out.println("path_or_url: path to file or URL to fetch from");
        System.out.println("format: newsapi or simple (default: determined from content)");
        System.out.println("\nExample:");
        System.out.println("java Client file ./data/newsapi.json newsapi");
        System.out.println("java Client url https://example.com/data/newsapi.json");
    }

    /**
     * Main method for running the client.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            return;
        }

        try (FileArticleLogger logger = new FileArticleLogger("parser_errors.log")) {
            ObjectMapper objectMapper = new ObjectMapper();
            ArticleValidator validator = new RequiredFieldsValidator();
            ArticlePrinter printer = new BasicArticlePrinter();
            ParserVisitor visitor = new ConcreteParserVisitor(objectMapper, validator, logger);

            Client client = new Client(printer, visitor);
            client.run(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Runs the client with the provided arguments.
     * @param args the command line arguments in the format: <source_type> <path_or_url> [format]
     *            where source_type is either "file" or "url",
     *            path_or_url is the path to file or URL to fetch from,
     *            and format is optionally "newsapi" or "simple"
     * @throws ClientException if an error occurs during execution
     * @throws IllegalArgumentException if insufficient or invalid arguments are provided
     */
    public void run(String[] args) throws ClientException {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException(
                    "Insufficient arguments. Required: <source_type> <path_or_url> [format]");
        }

        try {
            String sourceType = args[0];
            String pathOrUrl = args[1];
            String format = args.length > 2 ? args[2] : determineFormat(pathOrUrl);

            if (!sourceType.equals("file") && !sourceType.equals("url")) {
                throw new IllegalArgumentException(
                        "Invalid source type. Must be either 'file' or 'url'");
            }

            ParserConfiguration config = ParserConfigurationFactory.createConfiguration(
                    sourceType, format, pathOrUrl);

            Parser parser = config.accept(visitor);
            List<Article> articles = parser.parse(parser.getDataSource());
            articles.forEach(printer::print);
        } catch (ParserException e) {
            throw new ClientException("Failed to parse articles", e);
        }
    }

    /**
     * Determines the format based on the path or URL.
     * @param pathOrUrl The path or URL to analyze
     * @return The determined format (newsapi or simple)
     */
    private String determineFormat(String pathOrUrl) {
        String lowerPath = pathOrUrl.toLowerCase();
        if (lowerPath.contains("newsapi")) {
            return "newsapi";
        } else if (lowerPath.contains("simple")) {
            return "simple";
        }
        System.out.println("Warning: Could not determine format from path/url. Defaulting to NewsAPI format.");
        return "newsapi";
    }
}
