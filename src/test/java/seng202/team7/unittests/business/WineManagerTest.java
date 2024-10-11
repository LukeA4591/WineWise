package seng202.team7.unittests.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.business.WineManager;
import seng202.team7.business.WineryManager;
import seng202.team7.exceptions.DuplicateExc;
import seng202.team7.models.Wine;
import seng202.team7.models.Winery;
import seng202.team7.repository.DatabaseManager;

import java.util.Arrays;
import java.util.List;

public class WineManagerTest {

    private static WineManager wineManager;
    private static DatabaseManager databaseManager;

    Wine wine1;

    @BeforeAll
    public static void setup() throws DuplicateExc {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wineManager = new WineManager();
    }

    @BeforeEach
    public void freshDatabase() {
        databaseManager.resetDB();
    }

    private void populateDB() {
        wine1 = new Wine("Red", "Noir", "Winery", 2000, 85, "REGION 1", "High quality :)");
        Wine wine2 = new Wine("White", "Not Noir", "Winery", 2001, 95, "REGION 1", "Higher quality :)");
        Wine wine3 = new Wine("Red", "Noir again", "Diff. Winery", 2010, 80, "REGION 2", "'aight");

        wineManager.add(wine1);
        wineManager.add(wine2);
        wineManager.add(wine3);
    }

    /**
     * Creates two wineManagers and checks they are referencing the same instance of the wineDao to add to the
     * same database
     */
    @Test
    public void testSameInstance() {
        WineManager anotherWineManager = new WineManager();
        Assertions.assertEquals(0, wineManager.getAll().size());
        Assertions.assertEquals(0, anotherWineManager.getAll().size());
        populateDB();
        Assertions.assertEquals(3, wineManager.getAll().size());
        Assertions.assertEquals(3, anotherWineManager.getAll().size());
        // True Test
        anotherWineManager.add(new Wine("Red", "New wine to diff manager", "Winery", 2019, 75, "REGION 1", "secret"));
        Assertions.assertEquals(4, wineManager.getAll().size());
        Assertions.assertEquals(4, anotherWineManager.getAll().size());
    }
}
