package seng202.team7.business;

import seng202.team7.models.Winery;
import seng202.team7.repository.WineryDAO;

import java.util.List;
import java.util.Set;

public class WineryManager {
    private final WineryDAO wineryDAO;

    /**
     * Creates a new WineryManager object with its initialized WineDAO.
     * **/
    public WineryManager() { wineryDAO = new WineryDAO(); }

    public List<Winery> getAll() {
        return wineryDAO.getAll();
    }

    public int add(Winery winery) { return wineryDAO.add(winery); }

    public void addBatch(Set<Winery> wineries) {
        wineryDAO.addBatch(wineries);
    }

    public Set<String> getExistingWineryNames() {
        return wineryDAO.getExistingWineryNames();
    }

    public List<Winery> getAllWithNullLocation(String search) {return wineryDAO.getAllWithNullLocation(search); }

    public int updateLocationByWineryName(String wineryName, Float newLatitude, Float newLongitude) {
        return wineryDAO.updateLocationByWineryName(wineryName, newLatitude, newLongitude);
    }

    public List<Winery> getAllWithValidLocation() {return wineryDAO.getAllWithValidLocation(); }

    public Winery getWineryByName(String wineryName) {return wineryDAO.getWineryByName(wineryName); }
}
