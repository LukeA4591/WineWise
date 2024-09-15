package seng202.team0.models;

import java.util.ArrayList;
import java.util.List;

public class Rating {
    int rating;
    List<String> tastingNotes = new ArrayList<>();
    String review;
    String name;
    Wine wine;
    String address;

    /**
     * Constructor for Ratings for SQL that only holds score + review
     * @param rating
     * @param description
     */
    public Rating(int rating, String description, Wine wine) {
        this.rating = rating;
        this.review = description;
        this.wine = wine;
    }

    public Wine getWine() {
        return wine;
    }

    public void setWine(Wine wine) {
        this.wine = wine;
    }

    /**
     * The Getter method for the rating within a rating
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * The Getter method for the tasting notes a user believes a wine to have
     * @return tastingNotes
     */
    public List<String> getTastingNotes() {
        return tastingNotes;
    }

    /**
     * The Getter method for the review a users leaves on a wine
     * @return review
     */
    public String getReview() {
        return review;
    }

    /**
     * The Getter method for the name of the user who left the review
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * The Getter method for the address of the user who left the review
     * @return adress
     */
    public String getAddress() {
        return address;
    }

    /**
     * The Setter method for the rating a user leaves within a rating
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * The Setter method for the tasting notes a user believes a wine to have
     * @param tastingNotes
     */
    public void setTastingNotes(List<String> tastingNotes) {
        this.tastingNotes = tastingNotes;
    }

    /**
     * The Setter method for the review a user gives a wine
     * @param review
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * The Setter method for the name of the user who left the review
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The Setter method for the address of the user who left the review
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
