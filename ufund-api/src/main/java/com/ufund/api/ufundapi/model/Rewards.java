package com.ufund.api.ufundapi.model;

/**
 * Rewards class
 * 
 * Simple rewards class with title and description for each reward
 * @author Sophia Le
 */
public class Rewards {
    private String title;
    private String description;

    public Rewards(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
