package org.article.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the source of an article with an ID and name.
 * The {@code Source} class provides methods to access the source's ID and name.
 * <p>
 * It uses Jackson annotations for JSON deserialization.
 * </p>
 */
public class Source {
    private final String id;
    private final String name;
    /**
     * Constructs a Source with the provided ID and name.
     *
     * @param id   the ID of the source.
     * @param name the name of the source.
     */
    public Source(@JsonProperty("id") String id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the ID of the source.
     *
     * @return the ID of the source.
     */
    public String getId() { return id; }

    /**
     * Returns the name of the source.
     *
     * @return the name of the source.
     */
    public String getName() { return name; }
}