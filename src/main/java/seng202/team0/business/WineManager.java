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
     * Creates a new WineManager object. The WineDAO will be set and will allow the user to do all wine-based actions.
     * **/
    public WineManager() { wineDAO = new WineDAO(); }

    /**
     * Sends all entries in a file to the DAO using a specified importer.
     * @param importer Importer object (e.g. CSV importer)
     * @param file File with the same type as the importer
     * **/
    public void addAllWinesFromFile(Importable<Wine> importer, File file) {
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
     * Gets all wines from the DAO.
     * @return List of wines from the SQL database
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

    public int getWineID(Wine wine) { return wineDAO.getWineID(wine); }

    public List<Wine> getFilteredWines(Map<String, String> filters, Map<String, List<String>> scoreFilters) { return wineDAO.getFilteredWines(filters, scoreFilters); }

    public List<String> getDistinct(String column) { return wineDAO.getDistinct(column); }

    public void addBatch(List<Wine> wines) { wineDAO.addBatch(wines); }

    public List<Wine> getTopRated() { return wineDAO.getTopRated(); }

    public Wine getWineFromID(int wineID) { return wineDAO.getWineFromID(wineID); }

}
