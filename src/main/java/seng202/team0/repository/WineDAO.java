package seng202.team0.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;

import javax.xml.namespace.QName;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                wines.add(new Wine(rs.getString("type"), rs.getString("name"), rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"), rs.getString("region"), rs.getString("description")));
                // temp fix just using dev way of making wine
//                wines.add(new Wine(rs.getString("name")));
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
     * @return a list of the wines
     */
    public List<Wine> getFilteredWines(Map<String, String> filters) {
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
                    case "score":
                        sql.append(" AND score = ?");
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
        try(Connection conn = databaseManager.connect();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            for (Map.Entry<String, String> filter : filters.entrySet()) {
                String value = filter.getValue();
                if (!Objects.equals(value, "ALL")) {
                    ps.setObject(index++, value);
                }
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

    public List<String> getDistinct(String column) {
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
    }

    public List<Integer> getDistinctVintages() {
        List<Integer> vintages = new ArrayList<>();
        String sql = "SELECT DISTINCT vintage FROM wines";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vintages.add(rs.getInt("vintage"));
            }
            return vintages;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds single wine to database
     * @param toAdd object of type T to add
     * @return int
     * @throws DuplicateExc
     */
    @Override
    public int add(Wine toAdd) throws DuplicateExc {
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
//            ps.setString(8, "userRatings go here"); // TODO Find a way to ps.setLIST???

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


}
