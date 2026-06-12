package com.example.systemnews;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource: Data model representing a learning resource or item.
 * Stores information such as title, category, description, rating, and user feedback.
 */
public class Resource {
    private int id;
    private String title;
    private String url;
    private String description;
    private String category;
    private float averageRating;
    private int ratingCount;
    private String date;
    private List<UserFeedback> feedbacks;

    /**
     * UserFeedback: Nested class to store individual user comments and ratings.
     */
    public static class UserFeedback {
        private String comment;
        private float rating;

        public UserFeedback(String comment, float rating) {
            this.comment = comment;
            this.rating = rating;
        }

        public String getComment() { return comment; }
        public float getRating() { return rating; }
    }

    public Resource(int id, String title, String url, String description, String category, float initialRating, String date) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.category = category;
        this.averageRating = initialRating;
        this.ratingCount = 1; // Start with 1 to account for the initial rating
        this.date = date;
        this.feedbacks = new ArrayList<>();
    }

    // Getters for resource properties
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public float getAverageRating() { return averageRating; }
    public List<UserFeedback> getFeedbacks() { return feedbacks; }

    /**
     * addUserFeedback: Adds a new user rating and comment, and updates the average rating.
     * @param comment The user's written feedback.
     * @param rating The numerical star rating provided by the user.
     */
    public void addUserFeedback(String comment, float rating) {
        // Calculate new average rating: (old average * old count + new rating) / new count
        float totalRating = this.averageRating * this.ratingCount;
        this.ratingCount++;
        this.averageRating = (totalRating + rating) / this.ratingCount;
        // Store the feedback in the list
        this.feedbacks.add(new UserFeedback(comment, rating));
    }
}
