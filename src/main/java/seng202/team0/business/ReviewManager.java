package seng202.team0.business;

import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Review;
import seng202.team0.repository.ReviewDAO;

import java.util.List;

public class ReviewManager {

    private final ReviewDAO reviewDAO;

    public ReviewManager() {
        reviewDAO = new ReviewDAO();
    }

    public List<Review> getAll() { return reviewDAO.getAll(); }

    public List<Review> getReviewsByWineId(int wineID) { return reviewDAO.getReviewsByWineId(wineID); }

    public int add(Review toAdd) throws DuplicateExc { return reviewDAO.add(toAdd); }

    public void delete(int id) { reviewDAO.delete(id); }

    public void markAsReported(int id) { reviewDAO.markAsReported(id); }

    public void markAsUnreported(int id) { reviewDAO.markAsUnreported(id); }

    public List<Review> getFlaggedReviews() { return reviewDAO.getFlaggedReviews(); }

}
