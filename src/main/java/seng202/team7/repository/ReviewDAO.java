package seng202.team7.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Review;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * ReviewDAO class, interacts with the database to query reviews
 */
public class ReviewDAO implements DAOInterface<Review>{

    private static final Logger log = LogManager.getLogger(ReviewDAO.class);
    private final DatabaseManager databaseManager;
    private final WineDAO wineDao;

    /**
     * Gets a reference to the database singleton
     */
    public ReviewDAO(){
        databaseManager = DatabaseManager.getInstance();
        wineDao = new WineDAO();
    }

    /**
     * Gets all Reviews in the database
     * @return ratings list
     */
    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviews.add(new Review(rs.getInt("reviewID"), rs.getInt("rating"), rs.getString("description"), wineDao.getWineFromID(rs.getInt("wine"))));
            }
            return reviews;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    /**
     * Gets the all the reviews on a single wine
     * @param wineID ID of wine
     * @return list of all reviews
     */
    public List<Review> getReviewsByWineId(int wineID) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * from reviews WHERE wine=? AND reported = false";
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wineID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Review(rs.getInt("reviewID"), rs.getInt("rating"), rs.getString("description"), wineDao.getWineFromID(rs.getInt("wine"))));
            }
            return reviews;

        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a single review to the database
     * @param toAdd object of type T to add
     * @return int -1 if unsuccessful, otherwise the insertId
     * @throws DuplicateExc Throws error if there is a duplicate entry.
     */
    @Override
    public int add(Review toAdd) throws DuplicateExc {
        String sql = "INSERT INTO reviews (rating, description, wine) VALUES (?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toAdd.getRating());
            ps.setString(2, toAdd.getDescription());
            ps.setInt(3, wineDao.getWineID(toAdd.getWine()));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int insertId = -1;
            if (rs.next()) {
                insertId = rs.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Deletes a single review from the database with the id given
     * @param id The ID of the review that will be deleted.
     */
    public void delete(int id) {
        String sql = "DELETE FROM reviews WHERE reviewID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Marks a review as reported
     * @param id ID of review
     */
    public void markAsReported(int id) {
        String sql = "UPDATE reviews SET reported=true WHERE reviewID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Marks a review as unreported.
     * @param id ID of the review.
     */
    public void markAsUnreported(int id) {
        String sql = "UPDATE reviews SET reported=false WHERE reviewID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Gets all flagged reviews from the database.
     * @return List of flagged reviews.
     */
    public List<Review> getFlaggedReviews() {
        List<Review> flaggedReviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE reported = true";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                flaggedReviews.add(new Review(rs.getInt("reviewID"), rs.getInt("rating"), rs.getString("description"), wineDao.getWineFromID(rs.getInt("wine"))));
            }
            return flaggedReviews;

        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Gets the average of all reviews for each wine on a given page
     * @param page current page of the home pae
     * @return a linked hash map mapping the wine id to the average user review
     */
    public LinkedHashMap<Integer, Integer> getAverageReviews(int page) {
        LinkedHashMap<Integer, Integer> averageReviews = new LinkedHashMap<>();
        String sql = "SELECT wine, AVG(rating) AS average_rating FROM reviews WHERE reported=false GROUP BY wine ORDER BY average_rating DESC LIMIT 6 OFFSET ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, page * 6);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                averageReviews.put(rs.getInt("wine"), rs.getInt("average_rating"));
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return averageReviews;
    }

    /**
     * Method to get the number of wines with reviews
     * @return int number of wines with reviews
     */
    public int getNumWinesWithReviews() {
        int size = 0;
        String sql = "SELECT DISTINCT wine FROM reviews WHERE reported=false;";
        try (Connection conn = databaseManager.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                size++;
            }
            return size;
        } catch (SQLException e) {
            log.error(e);
            return size;
        }
    }
}
