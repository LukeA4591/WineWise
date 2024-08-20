package seng202.team0.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class AdminLoginService {

    /**
     * The singleton instance of AdminLoginService
     */
    private static AdminLoginService adminLoginInstance;

    /**
     * Boolean that stores if the admin is logged in
     */
    private boolean loggedIn = false;

    /**
     * Private constructor because AdminLoginService is a singleton
     * No arguments
     */
    private AdminLoginService() {
    }

    /**
     * Ensures that only once instance of AdminLoginService can be instantiated
     * @return the single instance of AdminLoginService
     */
    public static AdminLoginService getInstance() {
        if (adminLoginInstance == null) {
            adminLoginInstance = new AdminLoginService();
        }
        return adminLoginInstance;
    }

    /**
     * Used to ensure that admin is logged in before completing an admin restricted action
     * @return getter for Boolean loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Setter for Boolean loggedIn
     * @param loggedIn
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets the jar file path as a URI and converts it to a file type
     * @return The parent directory of jar file as a String
     */
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

    /**
     * gets the jar parents file path and appends "credentials.txt"
     * @return new file path to credentials.txt
     */
    private File getCredentialsFile() {
        String jarStr = this.getJarFilePath();
        jarStr += "/credentials.txt";
        File f = new File(jarStr);
        return f;
    }

    /**
     * checks to see if the credentials.txt file has already been created
     * if not calls the createCredentialsFile() method
     * @return A Boolean that WineEnvironment uses to launch different screens
     */
    public boolean createCredentialsFileIfNotExists() {
        File f = getCredentialsFile();
        if (!f.exists()) {
            // could do custom exception (then use a try catch)
            createCredentialsFile();
            return false; // first run
        }
        return true; // already exists
    }

    /**
     * creates a new empty file at the given directory
     * if it fails to create then app crashes
     */
    public void createCredentialsFile() {
        try {
            getCredentialsFile().createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * checks that password inputs are correct
     * @param password users input for the password text field
     * @param confirmPassword users input for the confirmation password text field
     * @return error message or lack thereof
     */
    public String checkPasswordConfirmation(String password, String confirmPassword) {
        if (password.length() == 0 || confirmPassword.length() == 0) {
            return "Please dont leave a field blank";
        } else if (!password.equals(confirmPassword)) {
            return "The two passwords do not match";
        } else if (password.length() < 8) {
            return "The password is under 8 characters";
        }
        return "";
    }

    /**
     * TODO incomplete
     * Hashes password and returns it
     * TODO dont use messageDigest for hashing as its not  secure
     * @param password
     * @return hashed password
     */
    public String hashPassword(String password) {
        return "##Hashed##"+password;
    }

    /**
     * writes the given username and hashes the password to credentials.txt
     * @param username users input for the password text field
     * @param password users input for the password text field
     */
    public void createNewUser(String username, String password) {
        try {
            FileWriter writeCredentialsToFile = new FileWriter(getCredentialsFile(), true);
            writeCredentialsToFile.write(username + System.lineSeparator());
            writeCredentialsToFile.write(hashPassword(password));
            writeCredentialsToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
