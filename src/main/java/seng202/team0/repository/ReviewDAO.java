package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Rating;
import seng202.team0.models.Wine;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements DAOInterface<Rating>{

    private static final Logger log = LogManager.getLogger(WineDAO.class);
    private final DatabaseManager databaseManager;


    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public ReviewDAO(){
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Gets all Reviews in the database
     * @return ratings list
     */
    @Override
    public List<Rating> getAll() {
        List<Rating> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviews.add(new Rating(rs.getInt("rating"), rs.getString("description"), rs.getString("wineName"),
                        rs.getString("wineWinery"), rs.getInt("wineVintage")));
            }
            return reviews;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    public List<Rating> getReviewsByWineId(Wine wine) {
        List<Rating> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE wineName = ? AND wineWinery = ? AND wineVintage = ?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, wine.getWineName());
            pstmt.setString(2, wine.getWineryString());
            pstmt.setInt(3, wine.getVintage());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Rating(rs.getInt("rating"), rs.getString("description"), rs.getString("wineName"),
                            rs.getString("wineWinery"), rs.getInt("wineVintage")));
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
     * @return
     * @throws DuplicateExc
     */
    @Override
    public int add(Rating toAdd) throws DuplicateExc {
        String sql = "INSERT INTO reviews (wineName, wineWinery, wineVintage, rating, description) VALUES (?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toAdd.getWineName());
            ps.setString(2, toAdd.getWinery());
            ps.setInt(3, toAdd.getVintage());
            ps.setInt(4, toAdd.getRating());
            ps.setString(5, toAdd.getReview()); // TODO added an object to database (risky)

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


}
