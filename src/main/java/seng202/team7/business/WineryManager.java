package seng202.team7.business;

import seng202.team7.models.Winery;
import seng202.team7.repository.WineryDAO;

import java.util.List;
import java.util.Set;

/**
 * Manager class for the WineryDAO, is utilized by controllers to access data from the database
 */
public class WineryManager {
    private final WineryDAO wineryDAO;

    /**
     * Creates a new WineryManager object with its initialized WineDAO.
     * **/
    public WineryManager() { wineryDAO = new WineryDAO(); }

    /**
     * Gets all wineries from the database
     * @return List of all wineries
     */
    public List<Winery> getAll() {
        return wineryDAO.getAll();
    }

    /**
     * Adds a single winery to the database
     * @param winery object of Winery type to add
     * @return 1 if successful, -1 if not
     */
    public int add(Winery winery) { return wineryDAO.add(winery); }

    /**
     * Adds a batch of wineries to the database
     * @param wineries List of Winery objects
     */
    public void addBatch(Set<Winery> wineries) {
        wineryDAO.addBatch(wineries);
    }

    /**
     * Gets the names of all existing wineries
     * @return Set of all existing winery names
     */
    public Set<String> getExistingWineryNames() {
        return wineryDAO.getExistingWineryNames();
    }

    /**
     * Gets all wineries without a location (not places on the map) matching the given search
     * @param search string from admin input
     * @return List of all wineries without a location whos names match the search
     */
    public List<Winery> getAllWithNullLocation(String search) {return wineryDAO.getAllWithNullLocation(search); }

    /**
     * Updates the location of the winery by the winery name
     * @param wineryName name of the winery
     * @param newLatitude new latitude of the winery
     * @param newLongitude new longitude of the winery
     * @return number of rows updated if successful, otherwise -1
     */
    public int updateLocationByWineryName(String wineryName, Float newLatitude, Float newLongitude) {
        return wineryDAO.updateLocationByWineryName(wineryName, newLatitude, newLongitude);
    }

    /**
     * Gets all wineries with a location that is not null (have been placed on the map)
     * @return List of all wineries with a valid location
     */
    public List<Winery> getAllWithValidLocation() {return wineryDAO.getAllWithValidLocation(); }

    /**
     * Gets a winery object based on the name of the winery
     * @param wineryName name of the winery where object is needed
     * @return Winery object with a matching name of the wineryName
     */
    public Winery getWineryByName(String wineryName) {return wineryDAO.getWineryByName(wineryName); }

    /**
     * Deletes a winery from the database
     * @param name name of the winery needing deleting
     */
    public void delete(String name) { wineryDAO.delete(name); }

    /**
     * Gets all wineries with a name like the search provided
     * @param wineryName query from user input
     * @return List of all wineries with names like the search
     */
    public List<Winery> getAllLikeSearch(String wineryName) { return wineryDAO.getAllLikeSearch(wineryName); }
}
