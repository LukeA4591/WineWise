package seng202.team7.business;

import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;
import seng202.team7.repository.ReviewDAO;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Handles all actions related to reviews. It acts as a request system for users to access the data in the database.
 * It allows the user to communicate with the review SQL database and perform actions such as getting reviews based on
 * its parent wine, flagging/unflagging reviews, and deleting reviews.
 */
public class ReviewManager {

    private final ReviewDAO reviewDAO;

    /**
     * Creates a new ReviewManager object with its initialized ReviewDAO.
     * **/
    public ReviewManager() {
        reviewDAO = new ReviewDAO();
    }

    /**
     * Gets all the reviews in the database.
     * @return A list of the reviews.
     */
    public List<Review> getAll() { return reviewDAO.getAll(); }

    /**
     * Gets all the reviews associated with a particular wine.
     * @param wineID The ID of the wine.
     * @return A list of the reviews.
     */
    public List<Review> getReviewsByWineId(int wineID) { return reviewDAO.getReviewsByWineId(wineID); }

    /**
     * Sends a single review to the DAO to be added.
     * @param toAdd The review object of the review.
     * @return int -1 if unsuccessful, otherwise the insertId
     * @throws DuplicateExc Throws error if there is a duplicate entry.
     */
    public int add(Review toAdd) throws DuplicateExc { return reviewDAO.add(toAdd); }

    /**
     * Deletes a single review from the database with the id given.
     * @param id The ID of the review.
     */
    public void delete(int id) { reviewDAO.delete(id); }

    /**
     * Marks a review as reported.
     * @param id ID of the review.
     */
    public void markAsReported(int id) { reviewDAO.markAsReported(id); }

    /**
     * Marks a review as unreported.
     * @param id ID of the review.
     */
    public void markAsUnreported(int id) { reviewDAO.markAsUnreported(id); }

    /**
     * Gets all flagged reviews from the database.
     * @return List of flagged reviews.
     */
    public List<Review> getFlaggedReviews() { return reviewDAO.getFlaggedReviews(); }

    /**
     * Gets the average of all reviews for each wine on a given page
     * @param page current page of the home pae
     * @return a linked hash map mapping the wine id to the average user review
     */
    public LinkedHashMap<Integer, Integer> getAverageReviews(int page) {return reviewDAO.getAverageReviews(page); }

    /**
     * Method to get the number of wines with reviews
     * @return int number of wines with reviews
     */
    public int getNumWinesWithReviews() {return reviewDAO.getNumWinesWithReviews(); }

}
