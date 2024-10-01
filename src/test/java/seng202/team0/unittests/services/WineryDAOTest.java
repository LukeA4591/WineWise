package seng202.team0.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.models.Winery;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineryDAO;

import java.util.HashSet;
import java.util.Set;

public class WineryDAOTest {
    private static WineryDAO wineryDAO;
    private static DatabaseManager databaseManager;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineryDAO = new WineryDAO();
    }

    @BeforeEach
    public void resetDB() {
        databaseManager.resetDB();
    }

    private void populateDatabase() throws DuplicateExc {
        wineryDAO.add(new Winery("One Winery", (float) 0, (float) 0));
        wineryDAO.add(new Winery("Two Winery", (float) 0, (float) 0));
        wineryDAO.add(new Winery("Three Winery", (float) 0, (float) 0));
    }

    @Test
    public void testGetAllWineries() throws DuplicateExc {
        populateDatabase();
        Assertions.assertEquals(3, wineryDAO.getAll().size());
    }

    @Test
    public void testAddWinery() throws DuplicateExc {
        populateDatabase();
        wineryDAO.add(new Winery("Four Winery", (float) 0, (float) 0));
        Assertions.assertEquals(4, wineryDAO.getAll().size());
    }

    @Test
    public void testAddBatch() throws DuplicateExc {
        Set<Winery> wineries = new HashSet<>();
        wineries.add(new Winery("One Winery", (float) 0, (float) 0));
        wineries.add(new Winery("Two Winery", (float) 0, (float) 0));
        wineries.add(new Winery("Three Winery", (float) 0, (float) 0));
        wineryDAO.addBatch(wineries);
        Assertions.assertEquals(wineries.size(), wineryDAO.getAll().size());
    }

    @Test
    public void testExistingWineries() throws DuplicateExc {
        populateDatabase();
        Assertions.assertEquals(3, wineryDAO.getExistingWineryNames().size());
    }
}
