package seng202.team0.unittests.services;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.repository.DatabaseManager;


import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    private DatabaseManager databaseManager;

    @BeforeEach
    public void setup() {
        databaseManager = DatabaseManager.getInstance();
    }

    @Test
    public void testDatabaseManagerInstance() {
        Assertions.assertNotNull(databaseManager);
        Assertions.assertEquals(databaseManager, DatabaseManager.getInstance());
    }

    @Test
    public void testDatabaseConnection() throws SQLException {
        Connection conn = databaseManager.connect();
        Assertions.assertNotNull(conn);
        Assertions.assertNotNull(conn.getMetaData());
        conn.close();
    }
}
