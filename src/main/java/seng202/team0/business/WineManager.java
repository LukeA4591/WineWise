package seng202.team0.business;

import seng202.team0.io.Importable;
import seng202.team0.models.Wine;
import seng202.team0.repository.WineDAO;

import java.io.File;
import java.util.List;

public class WineManager {
    private final WineDAO wineDAO;

    public WineManager() { wineDAO = new WineDAO(); }

    public void addAllWinesFromFile(Importable<Wine> importer, File file) {
        List<Wine> wines = importer.readFromFile(file);
        wineDAO.addBatch(wines);
    }

    public int addWine(Wine wine) { return wineDAO.add(wine); }

    public List<Wine> getAll() { return wineDAO.getAll(); }

    public void delete(String name, String winery, int vintage) { wineDAO.delete(name, winery, vintage); }
}
