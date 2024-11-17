package org.article.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.article.core.DataSource;
import org.article.core.Parser;
import org.article.util.ArticleLogger;
import org.article.core.Article;
import org.article.util.ArticleValidator;
import java.util.List;

/**
 * Parses data from the NewsAPI format.
 */
public abstract class NewsApiParser implements Parser {
    private final ObjectMapper objectMapper;
    private final ArticleValidator validator;
    private final ArticleLogger logger;

    /**
     * Constructs a NewsAPI parser with the provided dependencies.
     *
     * @param objectMapper the object mapper to use for parsing JSON
     * @param validator the validator to use for validating articles
     * @param logger the logger to use for logging messages
     */
    public NewsApiParser(ObjectMapper objectMapper, ArticleValidator validator, ArticleLogger logger) {
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.logger = logger;
    }

    @Override
    public List<Article> parse(DataSource source) throws ParserException {
        try {
            String data = source.getData();
            NewsApiResponse response = objectMapper.readValue(data, NewsApiResponse.class);

            if (response == null || response.getArticles() == null) {
                logger.error("Failed to parse NewsAPI response - null response or articles",
                        new IllegalStateException("Null response data"));
                return List.of();
            }

            return response.getArticles()
                    .stream()
                    .filter(article -> {
                        boolean isValid = validator.isValid(article);
                        if (!isValid) {
                            logger.warning("Article is missing required fields and will be skipped.");
                        }
                        return isValid;
                    })
                    .toList();
        } catch (IOException e) {
            logger.error("Error reading data from source", e);
            throw new ParserException("Error parsing NewsAPI format", e);
        } catch (Exception e) {
            logger.error("Error parsing NewsAPI data", e);
            throw new ParserException("Error parsing NewsAPI format", e);
        }
    }
}
