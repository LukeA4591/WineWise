package seng202.team7.business;

import seng202.team7.io.Importable;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.repository.WineDAO;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Handles all actions related to wines. It acts as a request system for users to access the data in the database.
 * It allows the user to communicate with the wine SQL database and perform actions such as getting wines based on
 * filters, adding entries, removing entries.
 * @author Ryan Hamilton
 * **/
public class WineManager {
    private final WineDAO wineDAO;

    private static int totalWinesInDB = 0;

    /**
     * Creates a new WineManager object with its initialized WineDAO.
     * **/
    public WineManager() { wineDAO = new WineDAO(); }

    /**
     * Sends all entries in a file to the DAO using a specified importer.
     * @param importer Importer object (e.g. CSV importer)
     * @param file File with the same type as the importer
     * @param headerIndexes Indexes of the headers for each column of data that the admin selected
     */
    public void addBatch(Importable<Wine> importer, File file, List<Integer> headerIndexes) {
        List<Wine> wines = importer.readFromFile(file, headerIndexes);
        wineDAO.addBatch(wines);
    }

    /**
     * Send a wine to the DAO to be added.
     * @param wine Wine to be added
     * @return Int to see if the wine was added to database (-1 is fail, else pass)
     * **/
    public int add(Wine wine) { return wineDAO.add(wine); }

    /**
     * Gets all the wines in the database.
     * @return List of wines
     * **/
    public List<Wine> getAll() { return wineDAO.getAll(); }

    /**
     * Deletes a wine.
     * @param name Name of the wine to be deleted
     * @param winery Winery of the wine to be deleted
     * @param vintage Vintage of the wine to be deleted.
     * **/
    public void delete(String name, String winery, int vintage) { wineDAO.delete(name, winery, vintage); }

    /**
     * Gets the ID of a wine based on its vintage, name, and wineryName which is stored in the wine object.
     * @param wine The wine object that will have its ID returned
     * @return The ID of the wine
     */
    public int getWineID(Wine wine) { return wineDAO.getWineID(wine); }

    /**
     * Gets textual and score filters from the search page and sends it to the DAO where it will receive a list of the
     * filtered wines.
     * @param filters A map of all the textual filters. Each filter (vintage, winery, etc.) will be mapped to the
     *                filter that the user selects for it.
     * @param scoreFilters A map of the score filter. The list in the map will contain the lower and upper bounds of
     *                     the score.
     * @param search The search input from the user.
     * @return List of filtered wines.
     */
    public List<Wine> getFilteredWines(Map<String, String> filters, Map<String, List<String>> scoreFilters, String search) { return wineDAO.getFilteredWines(filters, scoreFilters, search); }

    /**
     * Gets all the distinct strings or values in a column in the wine database.
     * @param column The name of the column.
     * @return A list of all the distinct items.
     */
    public List<String> getDistinct(String column) { return wineDAO.getDistinct(column); }

    /**
     * Gets a list of the top-rated wines, depending on the page of the home screen.
     * @param page page of the home screen that hte user is currently on
     * @return A list of the top wines.
     */
    public List<Wine> getTopRated(int page) { return wineDAO.getTopRated(page); }

    /**
     * Updates the wine and checks if it can be updated, returns true if successfully updated
     * @param toUpdate new wine details
     * @param oldWine old wine details
     * @return boolean depending on success of update
     */
    public boolean updateWine(Wine toUpdate, Wine oldWine) { return wineDAO.updateWine(toUpdate, oldWine); }

    /**
     * Creates a wine object using the attributes from the wineId's tuple.
     * @param wineID id of wine in the wines table.
     * @return Wine object containing attributes from the wineID tuple in the wines table.
     */
    public Wine getWineFromID(int wineID) { return wineDAO.getWineFromID(wineID); }

    /**
     * Gets a list of 3 top wines similar to the given wine in 1.colour 2.winery 3.vintage
     * @param wine wine which has been selected by the user.
     * @return A list of length 3 containing wines which are similar to the selected wine.
     */
    public List<Wine> getTheSimilarWines(Wine wine) {return wineDAO.getSimilarWines(wine); }

    /**
     * Searches the database for wines with text matching a search.
     * @param search text inputted by user.
     * @return list of wines matching the search.
     */
    public List<Wine> searchWines(String search) {return  wineDAO.searchWines(search);}

    /**
     * Checks if the details given already exists in the database.
     * @param wine wine to check if it already exists in the database.
     * @return boolean true if already exists, false if it doesn't.
     */
    public boolean checkIfWineExists(Wine wine) {return wineDAO.checkIfWineExists(wine);}

    /**
     * Method to get all wines associated with a winery
     * @param winery winery to check
     * @return list of all wines produced by the given winery
     */
    public List<Wine> getWineWithWinery(Winery winery) { return wineDAO.getWineWithWinery(winery); }

    /**
     * Method to get the total number of wines in the database
     * @return total number of wines in the database
     */
    public int getTotalWinesInDB() {
        return totalWinesInDB;
    }

    /**
     * Method to set the total number of wines in the database
     * @param totalWinesInDB total number of wines in the database
     */
    public void setTotalWinesInDB(int totalWinesInDB) {
        WineManager.totalWinesInDB = totalWinesInDB;
    }
}
