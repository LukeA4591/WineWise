package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;

import java.sql.*;
import java.util.*;

/**
 * WineDAO class, interacts with the database to query wines
 */
public class WineDAO implements DAOInterface<Wine> {

    private static final Logger log = LogManager.getLogger(WineDAO.class);
    private final DatabaseManager databaseManager;

    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public WineDAO(){
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Gets all wines in database
     * @return a list of all wines
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sql = "SELECT * FROM wines";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                wines.add(new Wine(
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("winery"),
                        rs.getInt("vintage"),
                        rs.getInt("score"),
                        rs.getString("region"),
                        rs.getString("description")));
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of wines depending on filters
     * @param filters map of filters
     * @param scoreFilters map of score filters
     * @return a list of the wines
     */
    public List<Wine> getFilteredWines(Map<String, String> filters, Map<String, List<String>> scoreFilters) {
        List<Wine> wines = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM wines WHERE 1=1");
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String key = filter.getKey();
            String value = filter.getValue();

            if (!Objects.equals(value, "ALL")) {
                switch (key) {
                    case "type":
                        sql.append(" AND type = ?");
                        break;
                    case "winery":
                        sql.append(" AND winery = ?");
                        break;
                    case "vintage":
                        sql.append(" AND vintage = ?");
                        break;
                    case "region":
                        sql.append(" AND region = ?");
                        break;
                }
            }
        }

        StringBuilder sqlScores = new StringBuilder("SELECT * FROM wines WHERE 1=1");
        boolean criticScoreIncluded = false;
        for (Map.Entry<String, List<String>> filter : scoreFilters.entrySet()) {
            if (!Objects.equals(filter.getValue().get(0), "")) {
                criticScoreIncluded = true;
                sqlScores.append(" AND score <= ? ");
                sqlScores.append(" AND score >= ? ");
            }

        }

        if (criticScoreIncluded) {
            sql.append(" INTERSECT ");
            sql.append(sqlScores.toString());
        }
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String value = filter.getValue();
                if (!Objects.equals(value, "ALL")) {
                    ps.setObject(index++, value);
                }
            }
            if (criticScoreIncluded) {
                List<String> bounds = scoreFilters.get("score");
                ps.setObject(index++, bounds.get(1));
                ps.setObject(index, bounds.get(0));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wines.add(new Wine(rs.getString("type"), rs.getString("name"), rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"), rs.getString("region"), rs.getString("description")));
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }

    }

    /**
     * Method for getting all the distinct values of a column from the wine table + SQL INJECTION Protection with hecking val column (WILL NEED TO CHANGE IF CHANGE WINE table)
     * @param column column which all the distinct values are needed
     * @return list of the distinct values
     */
    public List<String> getDistinct(String column) {
        List<String> validCols = List.of("wineID", "type", "name", "winery", "vintage", "score", "region", "description");
        if (validCols.contains(column)) {
            List<String> result = new ArrayList<>();
            String sql = "SELECT DISTINCT " + column + " FROM wines";
            try(Connection conn = databaseManager.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    result.add(rs.getString(column));
                }
                return result;
            } catch (SQLException sqlException) {
                log.error(sqlException);
                return new ArrayList<>();
            }
        } else {
            throw new IllegalArgumentException("Column " + column + " is not a valid column name");
        }
    }

    /**
     * Adds single wine to database
     * @param toAdd object of type Wine to add
     * @return int -1 if unsuccessful, otherwise the insertId
     */
    @Override
    public int add(Wine toAdd) {
        String sql = "INSERT INTO wines (type, name, winery, vintage, score, region, description) VALUES (?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toAdd.getColor());
            ps.setString(2, toAdd.getWineName());
            ps.setString(3, toAdd.getWineryString());
            ps.setInt(4, toAdd.getVintage());
            ps.setInt(5, toAdd.getScore());
            ps.setString(6, toAdd.getRegion());
            ps.setString(7, toAdd.getDescription());

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
     * Delete method to remove a wine from the database
     * @param name name of the wine
     * @param winery winery the wine is from
     * @param vintage vintage of the wine
     */
    public void delete(String name, String winery, int vintage) {
        String sql = "DELETE FROM wines WHERE name=? AND winery=? AND vintage=?;";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, winery);
            ps.setInt(3, vintage);

            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }


    /**
     * Adds a batch of wines to the database
     * @param wines list of wines to be added
     */
    public void addBatch (List <Wine> wines) {
//        for (Wine wine : wines) {
//            add(wine);
//        }
        String sql = "INSERT OR IGNORE INTO wines (type, name, winery, vintage, score, region, description) VALUES (?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Wine toAdd : wines) {
                ps.setString(1, toAdd.getColor());
                ps.setString(2, toAdd.getWineName());
                ps.setString(3, toAdd.getWineryString());
                ps.setInt(4, toAdd.getVintage());
                ps.setInt(5, toAdd.getScore());
                ps.setString(6, toAdd.getRegion());
                ps.setString(7, toAdd.getDescription());
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

    /**
     * Get the three top-rated wines do display on the home page of our application
     * @return a list of the top 3 rated wines
     */
    public  List<Wine> getTopRated() {
        List<Wine> topRated = new ArrayList<>();
        String sql = "SELECT * FROM wines ORDER BY score DESC LIMIT 3;";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                topRated.add(new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"),
                        rs.getString("region"), rs.getString("description")));
            }
            return topRated;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Gets an instance of a wine from the database based on the wine ID
     * @param wineID ID of the wine
     * @return wine which matches ID
     */
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

    /**
     * Gets an ID of a wine
     * @param toSearch wine being queried
     * @return ID of wine
     */
    public int getWineID(Wine toSearch) {
        int wineID;
        String sql = "SELECT wineID FROM wines WHERE name=? AND vintage=? AND winery=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toSearch.getWineName());
            ps.setInt(2, toSearch.getVintage());
            ps.setString(3, toSearch.getWineryString());
            ResultSet rs = ps.executeQuery();
            wineID = rs.getInt("wineID");
            return wineID;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return 0;
        }
    }

    /**
     * Updates the wine and checks if it can be updated, returns true if successfully updated
     * @param toUpdate new wine details
     * @param oldWine old wine details
     * @return success of update
     */
    public boolean updateWine(Wine toUpdate, Wine oldWine) {
        int id1 = getWineID(oldWine);
        int id2 = getWineID(toUpdate);
        if (id2 != 0 && id2 != id1) {
            return false;
        }
        String sql = "UPDATE wines SET type=?, name=?, winery=?, vintage=?, score=?, region=?, description=? WHERE wineID=?";
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toUpdate.getColor());
            ps.setString(2, toUpdate.getWineName());
            ps.setString(3, toUpdate.getWineryString());
            ps.setInt(4, toUpdate.getVintage());
            ps.setInt(5, toUpdate.getScore());
            ps.setString(6, toUpdate.getRegion());
            ps.setString(7, toUpdate.getDescription());
            ps.setInt(8, id1);
            ps.executeUpdate();
            return true;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return false;
        }
    }
}
