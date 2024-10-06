package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;

import java.sql.*;
import java.util.*;

import java.util.Random;

/**
 * WineDAO class, interacts with the database to query wines
 */
public class WineDAO implements DAOInterface<Wine> {

    private static final Logger log = LogManager.getLogger(WineDAO.class);
    private final DatabaseManager databaseManager;

    private WineryDAO wineryDAO = new WineryDAO();

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
        Set<Winery> uniqueWineries = new HashSet<>();
        for (Wine wine : wines) {
            Winery winery = new Winery(wine.getWineryString());
            uniqueWineries.add(winery);
        }
        Set<String> existingWineries = wineryDAO.getExistingWineryNames();
        Set<Winery> newWineries = new HashSet<>();
        for (Winery winery : uniqueWineries) {
            if (!existingWineries.contains(winery.getWineryName())) {
                newWineries.add(winery);
                existingWineries.add(winery.getWineryName());
            }
        }

        wineryDAO.addBatch(newWineries);
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



    /**
     * Returns a List of the top 3 wines with the 1st being same colour, 2nd being same winery, 3rd being same year
     * @param wine
     * @return
     */
    public List<Wine> getSimilarWines(Wine wine) {
        List<Wine> similarWines = new ArrayList<>();

        similarWines.add(getSimilarColour(wine));
        similarWines.add(getSimilarWinery(wine));
        similarWines.add(getSimilarVintage(wine));

        for (int i = 0; i < similarWines.size(); i++) {
            if (checkSameWine(similarWines.get(i), similarWines.get((i + 1) % similarWines.size()))) {
                List<Wine> the_list = new ArrayList<Wine>();
                the_list.add(wine);
                the_list.add(similarWines.get((i + 1) % similarWines.size()));
                the_list.add(similarWines.get((i + 2) % similarWines.size()));


                similarWines.set(i, reselectWine(i, the_list));
            }
        }


        return similarWines;
    }

    /**
     * Reselects a wine from the database in the same "category" (top type/winery/vintage) that is different from all current wines given
     * this is to ensure all recommended wines are different.
     * Note: first value in the list of wine to not be the same is always the original one we want to reference for which catagory
     * @param category
     * @param alreadyUsedWine
     */
    private Wine reselectWine(int category, List<Wine> alreadyUsedWine) {
        Wine NewWine;
        String sql = null;
        switch (category) {
            case 0:
                // New Colour/Type
                sql = "SELECT * FROM wines WHERE type=? ORDER BY score DESC LIMIT 5";
                break;
            case 1:
                // New Winery
                sql = "SELECT * FROM wines WHERE winery=? ORDER BY score DESC LIMIT 5";
                break;
            case 2:
                // New Vintage
                sql = "SELECT * FROM wines WHERE vintage=? ORDER BY score DESC LIMIT 5";
                break;
        }
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            switch (category) {
                case 0:
                    pstmt.setString(1, alreadyUsedWine.get(0).getColor());
                    break;
                case 1:
                    pstmt.setString(1, alreadyUsedWine.get(0).getWineryString());
                    break;
                case 2:
                    pstmt.setInt(1, alreadyUsedWine.get(0).getVintage());
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"),
                        rs.getString("region"), rs.getString("description"));

                if (checkSameWine(NewWine, alreadyUsedWine.get(0)) ||
                    checkSameWine(NewWine, alreadyUsedWine.get(1)) ||
                    checkSameWine(NewWine, alreadyUsedWine.get(2))) {
                    continue;
                } else {
                    return NewWine;
                }
            }

        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
        return null;
    }

    /**
     * Function to check if 2 given wines are the same by their unique values
     * @param wine1
     * @param wine2
     * @return true if they are the same wine
     */
    public boolean checkSameWine(Wine wine1, Wine wine2) {
        if (Objects.equals(wine1.getWineName(), wine2.getWineName()) && Objects.equals(wine1.getColor(), wine2.getColor()) &&  wine1.getVintage() == wine2.getVintage()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine of the same colour BUT if same wine return next highest
     * @param givenWine
     * @return top wine of same colour
     */
    public Wine getSimilarColour(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE type=? ORDER BY score DESC LIMIT 2";
        try(Connection conn = databaseManager.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, givenWine.getColor());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"),
                        rs.getString("region"), rs.getString("description"));

                if (checkSameWine(NewWine, givenWine)) {
                    continue;
                } else {
                    return NewWine;
                }
            }

            if (NewWine == null) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine from the same Winery BUT if same wine return next highest
     * Checks if NewWine being set to itself escapes while loop (case where only 1 wine from winery), returns random wine
     * @param givenWine
     * @return top-rated wine from the same Winery
     */
    public Wine getSimilarWinery(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE winery=? ORDER BY score DESC LIMIT 2";
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, givenWine.getWineryString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"),
                        rs.getString("region"), rs.getString("description"));
                if (checkSameWine(NewWine, givenWine)) {
                    continue;
                } else {
                    return NewWine;
                }
            }

            if (checkSameWine(NewWine, givenWine)) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine from the same Year BUT if same wine return next highest
     * @param givenWine
     * @return top-rated wine from the same Year
     */
    public Wine getSimilarVintage(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE vintage=? ORDER BY score DESC LIMIT 2";
        try(Connection conn = databaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, givenWine.getVintage());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), rs.getInt("score"),
                        rs.getString("region"), rs.getString("description"));
                if (checkSameWine(NewWine, givenWine)) {
                    continue;
                } else {
                    return NewWine;
                }

            }

            if (checkSameWine(NewWine, givenWine)) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * returns a Wine that is anything but the given wine
     * @param givenWine
     * @return
     */
    public Wine getRandomOtherWine(Wine givenWine) {
        List<Wine> RandomWines = getAll();
        Collections.shuffle(RandomWines);
        for (int i = 0; i < RandomWines.size(); i++) {
            if (checkSameWine(RandomWines.get(i), givenWine)) {
                continue;
            }else {
                return RandomWines.get(i);
            }
        }
        return givenWine;
    }
}
