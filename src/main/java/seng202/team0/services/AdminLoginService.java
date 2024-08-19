package seng202.team0.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class AdminLoginService {

    //attributes
    private static AdminLoginService adminLoginInstance;

    private boolean loggedIn = false;

    private AdminLoginService() {

    }

    public static AdminLoginService getInstance() {
        if (adminLoginInstance == null) {
            adminLoginInstance = new AdminLoginService();
        }
        return adminLoginInstance;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getJarFilePath() {
        try {
        // get the URI of the Jar
        URI jarURI = AdminLoginService.class.getProtectionDomain().getCodeSource().getLocation().toURI();

        // convert URI to a file type
        File jarFile = new File(jarURI);

        // get directory of jarFile
        return jarFile.getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    private File getCredentialsFile() {
        String jarStr = this.getJarFilePath();
        jarStr += "/credentials.txt";
        File f = new File(jarStr);
        return f;
    }

    public boolean createCredentialsFileIfNotExists() {
        File f = getCredentialsFile();
        if (!f.exists()) {
            // could do custom exception (then use a try catch)
            createCredentialsFile();
            return false; // first run
        }
        return true; // already exists
    }
    public void createCredentialsFile() {
        try {
            getCredentialsFile().createNewFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewUser(String username, String password) {
        File f = getCredentialsFile();

    }

}
