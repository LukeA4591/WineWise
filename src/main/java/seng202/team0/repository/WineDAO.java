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
                // todo fix how wine object / database will work together
                //wines.add(new Wine(rs.getString("type"), rs.getString("name"), rs.getInt("score"), rs.getInt("vintage"), rs.getString("region"), rs.getObject("winery", Winery.class<> Winery), rs.getString("description"), rs.getNString("userRatings")));
                // temp fix just using dev way of making wine
                wines.add(new Wine(rs.getString("name")));
            }
            return wines;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds single wine to database
     * @param toAdd object of type T to add
     * @return
     * @throws DuplicateExc
     */
    @Override
    public int add(Wine toAdd) throws DuplicateExc {
        String sql = "INSERT INTO wines (type, name, score, year, region, winery, description, userRatings) values (?,?,?,?,?,?,?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, toAdd.getColor());
            ps.setString(2, toAdd.getWineName());
            ps.setInt(3, toAdd.getScore());
            ps.setInt(4, toAdd.getVintage());
            ps.setString(5, toAdd.getRegion());
            ps.setObject(6, toAdd.getWinery());
            ps.setString(7, toAdd.getDescription());
            ps.setString(8, "userRatings go here"); // TODO Find a way to ps.setLIST???

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
