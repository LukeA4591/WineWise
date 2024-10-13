package seng202.team7.models;

/**
 * Review model, used to represent an instance of a review.
 */
public class Review {

    int reviewID;
    int rating;
    String description;
    Wine wine;
    Boolean reported;

    /**
     * Constructor for Ratings for SQL that only holds score + review
     * @param rating rating in review
     * @param description description of review
     * @param wine wine associated with review
     */
    public Review(int rating, String description, Wine wine) {
        this.rating = rating;
        this.description = description;
        this.wine = wine;
        this.reported = false;
    }

    /**
     * Constructor which contains reviewID
     * @param reviewID review id
     * @param rating rating in review
     * @param description description of review
     * @param wine wine related to review
     */
    public Review(int reviewID, int rating, String description, Wine wine) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.description = description;
        this.wine = wine;
        this.reported = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return reviewID == review.reviewID;
    }

    /**
     * The Getter method for the wine associated with the review
     * @return wine related to review
     */
    public Wine getWine() {
        return wine;
    }

    /**
     * The Getter method for the rating within a rating
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * The Getter method for the review a users leaves on a wine
     * @return review
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for whether the review has been reported
     * @param reported change to reported variable
     */
    public void setReported(Boolean reported) {
        this.reported = reported;
    }

    /**
     * Getter method for the id of the review
     * @return id of review
     */
    public int getReviewID()
    {
        return reviewID;
    }

    /**
     * The Getter for whether the review has been reported or not
     * @return reported boolean
     */
    public Boolean getReported() { return reported; }
}
