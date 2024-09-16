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
                reviews.add(new Rating(rs.getInt("rating"), rs.getString("review"), (Wine) rs.getObject("wine")));
            }
            return reviews;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    public int getWineID(Wine toSearch) {
        int wineID;
        String sql = "SELECT wineID " +
                "FROM wines " +
                "WHERE name = " + toSearch.getWineName() + " AND " +
                "vintage = " + toSearch.getVintage() + " AND " +
                "winery = " + toSearch.getWinery();

        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            wineID = rs.getInt(0); // TODO check if getInt works
            return wineID;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return 0;
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
        String sql = "INSERT INTO reviews (rating, description, wine) VALUES (?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, toAdd.getRating());
            ps.setString(2, toAdd.getReview());
            ps.setInt(3, getWineID(toAdd.getWine()));

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

    //TODO make reviewID be stored on rating model.
    public void markAsReported(int id) {
        String sql = "UPDATE FROM reviews WHERE reviewID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }


}
