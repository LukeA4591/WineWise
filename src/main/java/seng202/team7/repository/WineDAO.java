package seng202.team7.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;

import java.sql.*;
import java.util.*;


/**
 * WineDAO class, interacts with the database to query wines
 */
public class WineDAO implements DAOInterface<Wine> {

    private static final Logger log = LogManager.getLogger(WineDAO.class);
    private final DatabaseManager databaseManager;

    private List<Wine> alreadySelected = new ArrayList<>();

    private WineryDAO wineryDAO = new WineryDAO();

    /**
     * Creates a new UserDAO object and gets a reference to the database singleton
     */
    public WineDAO() {
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Method used for testing, clears the already selected list
     */
    public void clearAlreadySelected() {
        alreadySelected.clear();
    }

    /**
     * Gets all wines in database
     *
     * @return a list of all wines
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sql = "SELECT * FROM wines";
        try (Connection conn = databaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                wines.add(new Wine(
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("winery"),
                        rs.getInt("vintage"),
                        (Integer) rs.getObject("score"),
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
     * Gets a list of wines depending on filters and a search query, generates a sql string and then calls a helper method to execute it
     *
     * @param filters      User inputted string filters
     * @param scoreFilters User inputted integer filters
     * @param search       User inputted search query
     * @return List of filtered wines
     */
    public List<Wine> getFilteredWines(Map<String, String> filters, Map<String, List<String>> scoreFilters, String search) {
        boolean searchIncluded = false;
        StringBuilder sql = getSqlStringFilters(filters);

        StringBuilder sqlScores = new StringBuilder("SELECT * FROM wines WHERE 1=1");
        boolean criticScoreIncluded = false;
        for (Map.Entry<String, List<String>> filter : scoreFilters.entrySet()) {
            if (!Objects.equals(filter.getValue().getFirst(), "")) {
                criticScoreIncluded = true;
                sqlScores.append(" AND score <= ? ");
                sqlScores.append(" AND score >= ? ");
            }

        }

        if (criticScoreIncluded) {
            sql.append(" INTERSECT ");
            sql.append(sqlScores);
        }

        boolean isNum = search.matches("\\d+");
        StringBuilder sqlSearch;
        if (!isNum) {
            sqlSearch = new StringBuilder("SELECT * FROM wines WHERE " + "type LIKE ? OR " + "name LIKE ? OR "
                    + "winery LIKE ? OR " + "region LIKE ? OR " + "description LIKE ?");
        } else {
            sqlSearch = new StringBuilder("SELECT * FROM wines WHERE score = CAST(? AS INTEGER) OR vintage = CAST(? AS INTEGER)");
        }

        if (!search.isEmpty()) {
            sql.append(" INTERSECT ");
            sql.append(sqlSearch);
            searchIncluded = true;
        }

        return executeFilterSql(sql, filters, scoreFilters, criticScoreIncluded, searchIncluded, search, isNum);

    }

    /**
     * Helper function for getting filtered wines
     *
     * @param filters the string filters that the user has submitted
     * @return StringBuilder of the sql query getting string filters
     */
    private StringBuilder getSqlStringFilters(Map<String, String> filters) {
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
                    default:
                        break;
                }
            }
        }

        return sql;
    }

    /**
     * Helps the filtered wine method to execute the sql query
     *
     * @param sql                 sql query
     * @param filters             user inputted string filters
     * @param scoreFilters        user inputted score filters
     * @param criticScoreIncluded bool if critic score is included
     * @param searchIncluded      bool if search is included
     * @param search              search string
     * @param isNum               bool to determine if search in an integer
     * @return List of wines which contain the wines from the sql query
     */
    private List<Wine> executeFilterSql(StringBuilder sql, Map<String, String> filters, Map<String, List<String>> scoreFilters, boolean criticScoreIncluded, boolean searchIncluded, String search, boolean isNum) {
        List<Wine> wines = new ArrayList<>();
        try (Connection conn = databaseManager.connect();
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
                ps.setObject(index++, bounds.get(0));
            }
            if (searchIncluded) {
                if (!isNum) {
                    int indexMax = index + 4;
                    String formattedSearchText = "%" + search + "%";
                    for (int index2 = index; index2 <= indexMax; index2++) {
                        ps.setObject(index2, formattedSearchText);
                    }
                } else {
                    ps.setObject(index++, search);
                    ps.setObject(index, search);
                }
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wines.add(new Wine(rs.getString("type"), rs.getString("name"), rs.getString("winery"), rs.getInt("vintage"), (Integer) rs.getObject("score"), rs.getString("region"), rs.getString("description")));
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Method for getting all the distinct values of a column from the wine table + SQL INJECTION Protection with hecking val column (WILL NEED TO CHANGE IF CHANGE WINE table)
     *
     * @param column column which all the distinct values are needed
     * @return list of the distinct values
     */
    public List<String> getDistinct(String column) {
        List<String> validCols = List.of("wineID", "type", "name", "winery", "vintage", "score", "region", "description");
        if (validCols.contains(column)) {
            List<String> result = new ArrayList<>();
            String sql = "SELECT DISTINCT " + column + " FROM wines";
            try (Connection conn = databaseManager.connect();
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
     *
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
            ps.setObject(5, toAdd.getScore());
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
     *
     * @param name    name of the wine
     * @param winery  winery the wine is from
     * @param vintage vintage of the wine
     */
    public void delete(String name, String winery, int vintage) {
        String sql = "DELETE FROM wines WHERE name=? AND winery=? AND vintage=?;";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            ps.setString(1, name);
            ps.setString(2, winery);
            ps.setInt(3, vintage);

            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }


    /**
     * Adds a batch of wines to the database and sends a list of wines to the WineryDAO, so it can add any new wineries.
     *
     * @param wines list of wines to be added
     */
    public void addBatch(List<Wine> wines) {
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
        String sql = "INSERT OR IGNORE INTO wines (type, name, winery, vintage, score, region, description) VALUES (?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Wine toAdd : wines) {
                ps.setString(1, toAdd.getColor());
                ps.setString(2, toAdd.getWineName());
                ps.setString(3, toAdd.getWineryString());
                ps.setInt(4, toAdd.getVintage());
                ps.setObject(5, toAdd.getScore());
                ps.setString(6, toAdd.getRegion());
                ps.setString(7, toAdd.getDescription());
                ps.addBatch();
            }

            ps.executeBatch();

            conn.commit();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                log.info(rs.getLong(1));
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException sqlException) {
            log.error(sqlException);
        }
    }

    /**
     * Get the three top-rated wines do display on the home page of our application
     * @param page current page of the home page
     * @return a list of the top 3 rated wines
     */
    public List<Wine> getTopRated(int page) {
        List<Wine> topRated = new ArrayList<>();
        String sql = "SELECT * FROM wines ORDER BY score DESC LIMIT 6 OFFSET ?;";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, page * 6);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    topRated.add(new Wine(rs.getString("type"), rs.getString("name"),
                            rs.getString("winery"), rs.getInt("vintage"), (Integer) rs.getObject("score"),
                            rs.getString("region"), rs.getString("description")));
                }
            }
            return topRated;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }


    /**
     * Gets an instance of a wine from the database based on the wine ID
     *
     * @param wineID ID of the wine
     * @return wine which matches ID
     */
    public Wine getWineFromID(int wineID) {
        String sql = "SELECT * from wines WHERE wineID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, wineID);
            ResultSet rs = ps.executeQuery();

            return new Wine(
                    rs.getString("type"),
                    rs.getString("name"),
                    rs.getString("winery"),
                    rs.getInt("vintage"),
                    (Integer) rs.getObject("score"),
                    rs.getString("region"),
                    rs.getString("description"));
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return null;
        }
    }

    /**
     * Gets an ID of a wine
     *
     * @param toSearch wine being queried
     * @return ID of wine
     */
    public int getWineID(Wine toSearch) {
        int wineID;
        String sql = "SELECT wineID FROM wines WHERE name=? AND vintage=? AND winery=?";
        try (Connection conn = databaseManager.connect();
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
     *
     * @param toUpdate new wine details
     * @param oldWine  old wine details
     * @return success of update
     */
    public boolean updateWine(Wine toUpdate, Wine oldWine) {
        int id1 = getWineID(oldWine);
        int id2 = getWineID(toUpdate);
        if (id2 != 0 && id2 != id1) { //key attributes of toUpdate already exist
            return false;
        }
        String sql = "UPDATE wines SET type=?, name=?, winery=?, vintage=?, score=?, region=?, description=? WHERE wineID=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toUpdate.getColor());
            ps.setString(2, toUpdate.getWineName());
            ps.setString(3, toUpdate.getWineryString());
            ps.setInt(4, toUpdate.getVintage());
            ps.setObject(5, toUpdate.getScore());
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
     *
     * @param wine Wine displayed
     * @return List of wines that are similar to the displayed wine
     */
    public List<Wine> getSimilarWines(Wine wine) {

        alreadySelected.add(getSimilarColour(wine));
        alreadySelected.add(getSimilarWinery(wine));
        alreadySelected.add(getSimilarVintage(wine));

        return alreadySelected;
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine of the same colour BUT if same wine return next highest
     *
     * @param givenWine wine that has the type we want to be the same as
     * @return top wine of same colour
     */
    public Wine getSimilarColour(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE type=? ORDER BY score DESC LIMIT 2";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, givenWine.getColor());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), (Integer) rs.getObject("score"),
                        rs.getString("region"), rs.getString("description"));

                if (NewWine.equals(givenWine) || alreadySelected.contains(NewWine)) {
                    continue;
                } else {
                    return NewWine;
                }
            }

            if (NewWine == null || NewWine.equals(givenWine)) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getRandomOtherWine(givenWine);
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine from the same Winery BUT if same wine return next highest
     * Checks if NewWine being set to itself escapes while loop (case where only 1 wine from winery), returns random wine
     *
     * @param givenWine wine we want to be from the same winery
     * @return top-rated wine from the same Winery
     */
    public Wine getSimilarWinery(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE winery=? ORDER BY score DESC LIMIT 2";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, givenWine.getWineryString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), (Integer) rs.getObject("score"),
                        rs.getString("region"), rs.getString("description"));
                if (NewWine.equals(givenWine) || alreadySelected.contains(NewWine)) {
                    continue;
                } else {
                    return NewWine;
                }
            }

            if (NewWine == null || NewWine.equals(givenWine)) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getRandomOtherWine(givenWine);
    }

    /**
     * Used by getSimilarWines() - Returns the top-rated wine from the same Year BUT if same wine return next highest
     *
     * @param givenWine wine that we want to have the same year in
     * @return top-rated wine from the same Year
     */
    public Wine getSimilarVintage(Wine givenWine) {
        Wine NewWine = null;
        String sql = "SELECT * FROM wines WHERE vintage=? ORDER BY score DESC LIMIT 2";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, givenWine.getVintage());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                NewWine = new Wine(rs.getString("type"), rs.getString("name"),
                        rs.getString("winery"), rs.getInt("vintage"), (Integer) rs.getObject("score"),
                        rs.getString("region"), rs.getString("description"));
                if (NewWine.equals(givenWine) || alreadySelected.contains(NewWine)) {
                    continue;
                } else {
                    return NewWine;
                }

            }

            if (NewWine == null || NewWine.equals(givenWine)) {
                return getRandomOtherWine(givenWine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getRandomOtherWine(givenWine);
    }

    /**
     * returns a Wine that is anything but the given wine
     *
     * @param givenWine wine that we want the returned wine NOT TO BE
     * @return a wine that is anything but the given wine and wines already used
     */
    public Wine getRandomOtherWine(Wine givenWine) {
        List<Wine> randomWines = getAll();
        Collections.shuffle(randomWines);
        for (Wine randomWine : randomWines) {
            if (!randomWine.equals(givenWine) && !alreadySelected.contains(randomWine)) {
                return randomWine;
            } else {
                continue;
            }
        }
        return givenWine;
    }

    /**
     * Search method for getting wines like the search
     * @param searchText search bar text from user input
     * @return a list of all wines with types, names, wineries, regions, or score with a string matching the search field
     */
    public List<Wine> searchWines(String searchText) {
        List<Wine> wineList = new ArrayList<>();
        String sql = "SELECT * FROM wines WHERE " + "type LIKE ? OR " + "name LIKE ? OR "
                + "winery LIKE ? OR " + "region LIKE ? OR " + "description LIKE ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String formattedSearchText = "%" + searchText + "%";
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, formattedSearchText);
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Wine wine = new Wine(
                        resultSet.getString("type"),
                        resultSet.getString("name"),
                        resultSet.getString("winery"),
                        resultSet.getInt("vintage"),
                        (Integer) resultSet.getObject("score"),
                        resultSet.getString("region"),
                        resultSet.getString("description")
                );
                wineList.add(wine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wineList;
    }

    /**
     * Method to check if a wine already exists in the database
     * @param wine wine to check
     * @return boolean true if it exists, false if not
     */
    public boolean checkIfWineExists(Wine wine) {
        String sql = "SELECT * FROM wines WHERE vintage=? AND name=? AND winery=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wine.getVintage());
            pstmt.setString(2, wine.getWineName());
            pstmt.setString(3, wine.getWineryString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return true;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    /**
     * Method to get all wines associated with a winery
     * @param winery winery to check
     * @return list of all wines produced by the given winery
     */
    public List<Wine> getWineWithWinery(Winery winery) {
        List<Wine> wineList = new ArrayList<>();
        String sql = "SELECT * FROM wines WHERE winery=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, winery.getWineryName());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Wine wine = new Wine(
                        resultSet.getString("type"),
                        resultSet.getString("name"),
                        resultSet.getString("winery"),
                        resultSet.getInt("vintage"),
                        (Integer) resultSet.getObject("score"),
                        resultSet.getString("region"),
                        resultSet.getString("description")
                );
                wineList.add(wine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return wineList;
    }
}
