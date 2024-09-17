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
                reviews.add(new Rating(rs.getInt("reviewID"), rs.getInt("rating"), rs.getString("description"), getWineFromID(rs.getInt("wine"))));
            }
            return reviews;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    public Wine getWineFromID(int wineID) {
        String sql = "SELECT * from wines WHERE wineID=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, wineID);
            ResultSet rs = ps.executeQuery();
            Wine result = new Wine(
                    rs.getString("type"),
                    rs.getString("name"),
                    rs.getString("winery"),
                    rs.getInt("vintage"),
                    rs.getInt("score"),
                    rs.getString("region"),
                    rs.getString("description"));

            return result;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    public List<Rating> getReviewsByWineId(int wineID) {
        List<Rating> reviews = new ArrayList<>();
        String sql = "SELECT * from reviews WHERE wine=? AND reported = false";
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wineID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(new Rating(rs.getInt("reviewID"), rs.getInt("rating"), rs.getString("description"), getWineFromID(rs.getInt("wine"))));
            }
            return reviews;

        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }


    public int getWineID(Wine toSearch) {
        int wineID;
        String sql = "SELECT wineID FROM wines WHERE name=? AND vintage=? AND winery=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toSearch.getWineName());
            ps.setInt(2, toSearch.getVintage());
            ps.setString(3, toSearch.getWineryString());
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            wineID = rs.getInt("wineID");
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
        String sql = "UPDATE reviews SET reported=true WHERE reviewID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }


}
