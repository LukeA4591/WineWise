package seng202.team0.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.models.Winery;

import javax.xml.namespace.QName;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * Gets all users in database
     * @return a list of all users
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sql = "SELECT * FROM wines";
        try(Connection conn = databaseManager.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                wines.add(new Wine(rs.getString("type"), rs.getString("name"), rs.getInt("score"), rs.getInt("vintage"), rs.getString("region"), rs.getObject("winery", Winery.class<> Winery), rs.getString("description"), rs.getNString("userRatings")));
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }
}
