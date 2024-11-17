package org.article.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.article.core.Parser;
import org.article.core.DataSource;
import org.article.io.FileDataSource;
import org.article.io.URLDataSource;
import org.article.parser.NewsApiParser;
import org.article.parser.SimpleParser;
import org.article.util.ArticleLogger;
import org.article.util.ArticleValidator;

/**
 * Concrete implementation of ParserVisitor that creates parser instances.
 */
public class ConcreteParserVisitor implements ParserVisitor {
    private final ObjectMapper objectMapper;
    private final ArticleValidator validator;
    private final ArticleLogger logger;

    /**
     * Constructs a new ConcreteParserVisitor with required dependencies.
     * @param objectMapper The object mapper for JSON parsing
     * @param validator The validator for article validation
     * @param logger The logger for error logging
     */
    public ConcreteParserVisitor(ObjectMapper objectMapper, ArticleValidator validator, ArticleLogger logger) {
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.logger = logger;
    }

    @Override
    public Parser visitFileNewsApi(ParserConfiguration config) {
        FileNewsApiConfiguration fileConfig = (FileNewsApiConfiguration) config;
        return new NewsApiParser(objectMapper, validator, logger) {
            private final DataSource source = new FileDataSource(fileConfig.getFilePath());

            @Override
            public DataSource getDataSource() {
                return source;
            }
        };
    }

    @Override
    public Parser visitFileSimple(ParserConfiguration config) {
        FileSimpleConfiguration fileConfig = (FileSimpleConfiguration) config;
        return new SimpleParser(objectMapper, validator, logger) {
            private final DataSource source = new FileDataSource(fileConfig.getFilePath());

            @Override
            public DataSource getDataSource() {
                return source;
            }
        };
    }

    @Override
    public Parser visitUrlNewsApi(ParserConfiguration config) {
        UrlNewsApiConfiguration urlConfig = (UrlNewsApiConfiguration) config;
        return new NewsApiParser(objectMapper, validator, logger) {
            private final DataSource source = new URLDataSource(urlConfig.getUrl());

            @Override
            public DataSource getDataSource() {
                return source;
            }
        };
    }
}