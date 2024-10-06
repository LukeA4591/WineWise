package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;

import java.util.*;
import java.sql.*;

public class WineryDAO implements DAOInterface<Winery> {

    private static final Logger log = LogManager.getLogger(WineDAO.class);
    private final DatabaseManager databaseManager;

    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public WineryDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    //TODO test this method:
    @Override
    public List<Winery> getAll() {
        List<Winery> wineries = new ArrayList<>();
        String sql = "SELECT * FROM wineries;";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Float longitude = rs.getObject("longitude") != null ? rs.getFloat("longitude"): null;
                Float latitude = rs.getObject("latitude") != null ? rs.getFloat("latitude"): null;
                wineries.add(new Winery(rs.getString("wineryName"), longitude, latitude));
            }
            return wineries;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    @Override
    public int add(Winery toAdd) {
        String sql = "INSERT INTO wineries (wineryName, longitude, latitude) VALUES (?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toAdd.getWineryName());
            if (toAdd.getLatitude() != null && toAdd.getLatitude() != null) {
                ps.setFloat(2, toAdd.getLongitude());
                ps.setFloat(3, toAdd.getLatitude());
            }
            ps.executeUpdate();
            return 1;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }

    }

    //TODO not sure about the duplicateExc
    public void addBatch (Set <Winery> wineries) {
        String sql = "INSERT OR IGNORE INTO wineries (wineryName, longitude, latitude) VALUES (?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Winery toAdd : wineries) {
                ps.setString(1, toAdd.getWineryName());
                if (toAdd.getLatitude() != null && toAdd.getLatitude() != null) {
                    ps.setFloat(2, toAdd.getLongitude());
                    ps.setFloat(3, toAdd.getLatitude());
                }
                ps.addBatch();
            }
            ps.executeBatch();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                log.info(rs.getLong(1));
            }
            conn.commit();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    public Set<String> getExistingWineryNames() {
        Set<String> existingWineryNames = new HashSet<>();
        String sql = "SELECT wineryName FROM wineries;";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                existingWineryNames.add(rs.getString("wineryName"));
            }
            return existingWineryNames;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new HashSet<>();
        }
    }


}