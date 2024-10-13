package seng202.team7.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.models.Winery;

import java.util.*;
import java.sql.*;

/**
 * Database Access Object for the Winery class, used to get Winery data from the database
 */
public class WineryDAO implements DAOInterface<Winery> {

    private static final Logger log = LogManager.getLogger(WineryDAO.class);
    private final DatabaseManager databaseManager;

    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public WineryDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Gets all wineries from the database
     * @return List of all wineries
     */
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

    /**
     * Adds a single winery to the database
     * @param toAdd object of Winery type to add
     * @return 1 if successful, -1 if not
     */
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

    /**
     * Adds a batch of wineries to the database
     * @param wineries List of Winery objects
     */
    public void addBatch (Set <Winery> wineries) {
        String sql = "INSERT OR IGNORE INTO wineries (wineryName, longitude, latitude) VALUES (?, ?, ?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
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
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Gets the names of all existing wineries
     * @return Set of all existing winery names
     */
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

    /**
     * Gets all wineries without a location (not places on the map) matching the given search
     * @param search string from admin input
     * @return List of all wineries without a location whos names match the search
     */
    public List<Winery> getAllWithNullLocation(String search) {
        List<Winery> wineries = new ArrayList<>();
        String sql = "SELECT * FROM wineries WHERE latitude IS NULL AND longitude IS NULL";
        if (!Objects.equals(search, "")) {
            search = search + "%";
            sql += " AND wineryName LIKE ? ORDER BY wineryName;";
        } else {
            sql += " ORDER BY wineryName;";
        }
        try (Connection conn = databaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!Objects.equals(search, "")) {
                stmt.setString(1, search);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                wineries.add(new Winery(rs.getString("wineryName"), null, null));
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
        return wineries;
    }


    /**
     * Updates the location of the winery by the winery name
     * @param wineryName name of the winery
     * @param newLatitude new latitude of the winery
     * @param newLongitude new longitude of the winery
     * @return number of rows updated if successful, otherwise -1
     */
    public int updateLocationByWineryName(String wineryName, Float newLatitude, Float newLongitude) {
        String sql = "UPDATE wineries SET latitude = ?, longitude = ? WHERE wineryName = ?;";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, newLatitude, java.sql.Types.REAL);
            ps.setObject(2, newLongitude, java.sql.Types.REAL);
            ps.setString(3, wineryName);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated;  // Return the number of rows updated directly
        } catch (SQLException sqlException) {
            log.error("Failed to update location for winery: " + wineryName, sqlException);
            return -1;
        }
    }

    /**
     * Gets all wineries with a location that is not null (have been placed on the map)
     * @return List of all wineries with a valid location
     */
    public List<Winery> getAllWithValidLocation() {
        List<Winery> wineries = new ArrayList<>();
        String sql = "SELECT * FROM wineries WHERE latitude IS NOT NULL AND longitude IS NOT NULL;";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Float longitude = rs.getFloat("longitude");
                Float latitude = rs.getFloat("latitude");
                wineries.add(new Winery(rs.getString("wineryName"), longitude, latitude));
            }
            return wineries;
        } catch (SQLException sqlException) {
            log.error("Failed to retrieve wineries with valid location", sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a winery object based on the name of the winery
     * @param wineryName name of the winery where object is needed
     * @return Winery object with a matching name of the wineryName
     */
    public Winery getWineryByName(String wineryName) {
        String sql = "SELECT * FROM wineries WHERE wineryName = ?;";
        Winery winery = null;

        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, wineryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("wineryName");
                    Float longitude = rs.getObject("longitude") != null ? rs.getFloat("longitude") : null;
                    Float latitude = rs.getObject("latitude") != null ? rs.getFloat("latitude") : null;
                    winery = new Winery(name, longitude, latitude);
                }
            }
        } catch (SQLException sqlException) {
            log.error("Failed to get winery by name: " + wineryName, sqlException);
        }
        return winery;
    }

    /**
     * Deletes a winery from the database
     * @param name name of the winery needing deleting
     */
    public void delete(String name) {
        String sql = "DELETE FROM wineries WHERE wineryName=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Gets all wineries with a name like the search provided
     * @param wineryName query from user input
     * @return List of all wineries with names like the search
     */
    public List<Winery> getAllLikeSearch(String wineryName) {
        List<Winery> wineries = new ArrayList<>();
        String sql = "SELECT * FROM wineries WHERE wineryName LIKE ? ORDER BY wineryName;";
        try (Connection conn = databaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (!Objects.equals(wineryName, "")) {
                wineryName = wineryName + "%";
                stmt.setString(1, wineryName);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("wineryName");
                Float longitude = rs.getObject("longitude") != null ? rs.getFloat("longitude") : null;
                Float latitude = rs.getObject("latitude") != null ? rs.getFloat("latitude") : null;
                wineries.add(new Winery(name, longitude, latitude));
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
        return wineries;
    }
}