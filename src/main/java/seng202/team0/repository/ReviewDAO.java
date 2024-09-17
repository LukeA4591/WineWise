package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Review;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ReviewDAO class, interacts with the database to query reviews
 */
public class ReviewDAO implements DAOInterface<Review>{
    private static final Logger log = LogManager.getLogger(WineDAO.class);
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
            return null;
        }
    }


    /**
     * Adds a single review to the database
     * @param toAdd object of type T to add
     * @return
     * @throws DuplicateExc
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
     * @param id
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
     * Marks a review as unreported
     * @param id ID of review
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
     * Gets all flagged reviews from the database
     * @return list of flagged reviews
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
}
