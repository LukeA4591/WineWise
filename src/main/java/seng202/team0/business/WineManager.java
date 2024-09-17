package seng202.team0.business;

import seng202.team0.io.Importable;
import seng202.team0.models.Wine;
import seng202.team0.repository.WineDAO;

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

    /**
     * Creates a new WineManager object with its initialized WineDAO.
     * **/
    public WineManager() { wineDAO = new WineDAO(); }

    /**
     * Sends all entries in a file to the DAO using a specified importer.
     * @param importer Importer object (e.g. CSV importer)
     * @param file File with the same type as the importer
     * **/
    public void addBatch(Importable<Wine> importer, File file) {
        List<Wine> wines = importer.readFromFile(file);
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
     * @return List of wines from the SQL database
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
     * @return A list of wines based on the filters.
     */
    public List<Wine> getFilteredWines(Map<String, String> filters, Map<String, List<String>> scoreFilters) { return wineDAO.getFilteredWines(filters, scoreFilters); }

    /**
     * Gets all the distinct strings or values in a column in the wine database.
     * @param column The name of the column.
     * @return A list of all the distinct items.
     */
    public List<String> getDistinct(String column) { return wineDAO.getDistinct(column); }

    /**
     * Gets a list of the top 3 rated wines in the wine database.
     * @return A list of the top wines.
     */
    public List<Wine> getTopRated() { return wineDAO.getTopRated(); }

}
