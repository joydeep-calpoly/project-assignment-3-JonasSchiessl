package org.article.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.article.core.Article;
import org.article.core.DataSource;
import org.article.core.Parser;
import org.article.core.Source;
import org.article.util.ArticleLogger;
import org.article.util.ArticleValidator;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Parses data from the simple article format.
 */
public abstract class SimpleParser implements Parser {
    private final ObjectMapper objectMapper;
    private final ArticleValidator validator;
    private final ArticleLogger logger;

    /**
     * Constructs a simple parser with the provided dependencies.
     *
     * @param objectMapper the object mapper to use for parsing JSON
     * @param validator the validator to use for validating articles
     * @param logger the logger to use for logging messages
     */
    public SimpleParser(ObjectMapper objectMapper, ArticleValidator validator, ArticleLogger logger) {
        this.objectMapper = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.validator = validator;
        this.logger = logger;
    }

    /**
     * Parses the data from the provided source into a list of articles.
     *
     * @param source the data source to parse
     * @return the list of articles parsed from the source
     * @throws ParserException if an error occurs while parsing the source
     */
    @Override
    public List<Article> parse(DataSource source) throws ParserException {
        try {
            String data = source.getData();
            return parseSingleOrArrayFormat(data);
        } catch (IOException e) {
            logger.error("Error reading data from source", e);
            throw new ParserException("Error reading source data", e);
        }
    }

    private List<Article> parseSingleOrArrayFormat(String data) throws ParserException {
        try {
            return parseSingleArticle(data);
        } catch (ParserException e) {
            return parseArticleArray(data);
        }
    }

    /**
     * Parses the data from the provided source into a list of articles.
     *
     * @param data the JSON data to parse
     * @return the list of articles parsed from the data
     * @throws ParserException if an error occurs while parsing the data
     */
    private List<Article> parseSingleArticle(String data) throws ParserException {
        try {
            SimpleArticle simpleArticle = objectMapper.readValue(data, SimpleArticle.class);
            if (simpleArticle == null) {
                logger.warning("Parsed article is null");
                return List.of();
            }
            Article article = convertToArticle(simpleArticle);
            if (validator.isValid(article)) {
                return List.of(article);
            } else {
                logger.warning("Article is missing required fields and will be skipped.");
                return List.of();
            }
        } catch (IOException e) {
            throw new ParserException("Error parsing single article format", e);
        }
    }

    /**
     * Parses an array of simple articles into a list of articles.
     *
     * @param data the JSON data to parse
     * @return the list of articles parsed from the data
     * @throws ParserException if an error occurs while parsing the data
     */
    private List<Article> parseArticleArray(String data) throws ParserException {
        try {
            SimpleArticle[] articleArray = objectMapper.readValue(data, SimpleArticle[].class);
            if (articleArray == null) {
                logger.warning("Parsed article array is null");
                return List.of();
            }
            return Stream.of(articleArray)
                    .filter(Objects::nonNull)
                    .map(this::convertToArticle)
                    .filter(article -> {
                        boolean isValid = validator.isValid(article);
                        if (!isValid) {
                            logger.warning("Article is missing required fields and will be skipped.");
                        }
                        return isValid;
                    })
                    .toList();
        } catch (IOException e) {
            logger.error("Failed to parse article array format", e);
            throw new ParserException("Error parsing article array format", e);
        }
    }

    /**
     * Converts a simple article to an article.
     *
     * @param simple the simple article to convert
     * @return the converted article
     */
    private Article convertToArticle(SimpleArticle simple) {
        if (simple == null) {
            return null;
        }
        return new Article(
                simple.getTitle(),
                simple.getDescription(),
                simple.getDate(),
                simple.getUrl(),
                null,                       // urlToImage not in simple format
                null,                       // content not in simple format
                new Source(null, "Simple"), // default source since not in format
                null                        // author not in simple format
        );
    }
}