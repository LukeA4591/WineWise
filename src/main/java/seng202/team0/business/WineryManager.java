package seng202.team0.business;

import seng202.team0.models.Winery;
import seng202.team0.repository.WineryDAO;

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

    public void delete(String name) { wineryDAO.delete(name); }
}
