package seng202.team7.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team7.exceptions.DuplicateExc;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * DatabaseManager class which manages the state of the database
 */
public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private final String url;

    /**
     * Private constructor for singleton purposes
     * Creates database if it does not already exist in specified location
     */
    private DatabaseManager(String urlIn) {
        if (urlIn == null || urlIn.isEmpty()) {
            this.url = getDatabasePath();
        } else {
            this.url = urlIn;
        }
        if (!checkDatabaseExists(url)) {
            createDatabaseFile(url);
            resetDB();
        }
    }

    /**
     * Initializer for created an instance of the database with a specified url
     *
     * @param url string url of database to load
     * @return singleton instance
     * @throws DuplicateExc if there is already a singleton instance
     */
    public static DatabaseManager initialiseInstanceWithUrl(String url) throws DuplicateExc {
        if (instance == null)
            instance = new DatabaseManager(url);
        else
            throw new DuplicateExc("Database Manager instance already exists, cannot create with url: " + url);

        return instance;
    }

    /**
     * Singleton method, returns current instance of the database, creates it if not
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance() {
        if (instance == null)
            // Uncomment this line to use in IntelliJ
//            instance = new DatabaseManager("jdbc:sqlite:./src/main/resources/database.db");
            instance = new DatabaseManager(null);

        return instance;
    }

    /**
     * Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Method to connect to the database
     * @return database connection
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            log.error(e);
        }
        return conn;
    }

    /**
     * Initialises the database if it does not exist using the sql script included in resources
     */
    public void resetDB() {
        try {
            InputStream in = getClass().getResourceAsStream("/sql/initialise_database.sql");
            executeSQLScript(in);
        } catch (NullPointerException e) {
            log.error("Error loading database initialisation file", e);
        }
    }

    /**
     * Gets path to the database relative to the jar file
     * @return jdbc encoded url location of database
     */
    private String getDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/database.db";
    }

    /**
     * Check that a database exists in the expected location
     * @param url expected location to check for database
     * @return True if database exists else false
     */
    private boolean checkDatabaseExists(String url) {
        File f = new File(url.substring(12));
        return f.exists();
    }

    /**
     * Creates a database file at the location specified by the url
     * @param url url to creat database at
     */
    private void createDatabaseFile(String url) {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                String metaDriverLog = String.format("A new database has been created. The driver name is %s", meta.getDriverName());
                log.info(metaDriverLog);
            }
        } catch (SQLException e) {
            log.error(String.format("Error creating new database file url:%s", url));
            log.error(e);
        }
    }

    /**
     * Reads and executes all statements within the sql file provided
     * @param sqlFile input stream of file containing sql statements for execution (separated by --SPLIT)
     */
    private void executeSQLScript(InputStream sqlFile) {
        String s;
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sqlFile))) {
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }

            String[] individualStatements = sb.toString().split("--SPLIT");
            try (Connection conn = this.connect();
                 Statement statement = conn.createStatement()) {
                for (String singleStatement : individualStatements) {
                    statement.executeUpdate(singleStatement);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Error could not find specified database initialisation file", e);
        } catch (IOException e) {
            log.error("Error working with database initialisation file", e);
        } catch (SQLException e) {
            log.error("Error executing sql statements in database initialisation file", e);
        }
    }
}
