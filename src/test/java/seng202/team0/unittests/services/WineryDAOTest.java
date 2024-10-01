package seng202.team0.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import seng202.team0.exceptions.DuplicateExc;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.WineryDAO;

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
}
