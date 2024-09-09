package seng202.team0.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.exceptions.DuplicateExc;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance = null;
    private static final Logger log = LogManager.getLogger(DatabaseManager.class);
    private final String url;

    /**
     * Private constructor for singleton purposes
     * Creates database if it does not already exist in specified location
     * <p>
     */
    private DatabaseManager(String urlIn) {
        if (urlIn == null || urlIn.isEmpty()) {
            System.out.println("GETTING DATABASE PATH");
            this.url = getDatabasePath();
        } else {
            System.out.println("this.url = urlIN");
            this.url = urlIn;
        }
        if (!checkDatabaseExists(url)) {
            System.out.println("######################### CREATING DB FIELD AND resetDB() #########################");
            createDatabaseFile(url);
            resetDB();
        }
    }

    /**
     * WARNING Allows for setting specific database url (currently only needed for test databases, but may be useful
     * in future) USE WITH CAUTION. This does not override the current singleton instance so must be the first call.
     *
     * @param url string url of database to load (this needs to be full url e.g. "jdbc:sqlite:./src/...")
     * @return current singleton instance
     * @throws DuplicateExc if there is already a singleton instance
     */
    private static DatabaseManager initialiseInstanceWithUrl(String url) throws DuplicateExc {
        if (instance == null)
            instance = new DatabaseManager(url);
        else
            throw new DuplicateExc("Database Manager instance already exists, cannot create with url: " + url);

        return instance;
    }

    /**
     * Singleton method to get current Instance if exists otherwise create it
     *
     * @return the single instance DatabaseSingleton
     */
    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager("jdbc:sqlite:./src/main/resources/database.db");
//            instance = new DatabaseManager(null);

        return instance;
    }

    /**
     * WARNING Sets the current singleton instance to null
     */
    public static void REMOVE_INSTANCE() {
        instance = null;
    }

    /**
     * Connect to the database
     *
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

    public void drop_it() {
        String our_path = instance.getDatabasePath();
        Connection conn = null;
        Statement stmt = null;

        if (this.url == null) {
            System.out.println("URL IS NULL");
        } else {
            System.out.println(this.url);
            try {
                conn = DriverManager.getConnection(our_path);
                stmt = conn.createStatement();

                String sql = "DROP TABLE IF EXISTS wines";

                stmt.executeUpdate(sql);

                System.out.println("Table wines dropped");

//                stmt = conn.createStatement();
//                String sqlCommand = "DROP TABLE wines;";
//                stmt.executeUpdate(sqlCommand);
//                stmt.close();
//                conn.commit();
//                conn.close();
            } catch (SQLException e) {
                System.err.println("SQL error occurred.");
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

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
     *
     * @return jdbc encoded url location of database
     */
    private String getDatabasePath() {
        String path = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File jarDir = new File(path);
        return "jdbc:sqlite:" + jarDir.getParentFile() + "/database.db";
    }

    public boolean getReturnIfExists() {
        return checkDatabaseExists(getDatabasePath());
    }

    /**
     * Check that a database exists in the expected location
     *
     * @param url expected location to check for database
     * @return True if database exists else false
     */
    private boolean checkDatabaseExists(String url) {
        File f = new File(url.substring(12));
        return f.exists();
    }

    /**
     * Creates a database file at the location specified by the url
     *
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
     * Note that each statement must be separated by '--SPLIT' this is not a desired limitation but allows for a much
     * wider range of statement types.
     *
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
