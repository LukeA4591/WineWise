package seng202.team0.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;


public class AdminLoginService {

    /**
     * A constant holding the length of the salt in bytes for password hashing.
     */
    private static final int SALT_LENGTH = 16;

    /**
     * A constant holding the length of the hash in bytes.
     */
    private static final int HASH_LENGTH = 32;

    /**
     * Number of times the hash function is applied.
     * The greater the number the more secure, but also intensive it is.
     */
    private static final int ITERATION_COUNT = 1000;

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
    public byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }


    /**
     * Hashes password and returns it
     * salt is a byte array which is randomly filled, it is used to create a more secure hash.
     * The keyLength is in bits hence the 'HASH_LENGTH * 8'
     *
     * TODO dont use messageDigest for hashing as its not  secure
     * @param password
     * @return hashed password
     */
    public String hashPasswordWithSalt(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, HASH_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        byte[] saltAndHash = new byte[SALT_LENGTH + hash.length];
        System.arraycopy(salt, 0, saltAndHash, 0, SALT_LENGTH);
        System.arraycopy(hash, 0, saltAndHash, SALT_LENGTH, hash.length);

        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    public byte[] getSalt(String storedHash) {
        byte[] decodedBytes = Base64.getDecoder().decode(storedHash);
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(decodedBytes, 0, salt, 0, SALT_LENGTH);
        return salt;
    }

    public String convertStoredHashToString(String storedHash) {
        byte[] decodedBytes = Base64.getDecoder().decode(storedHash);
        return Base64.getEncoder().encodeToString(decodedBytes);
    }


    /**
     * writes the given username and hashes the password to credentials.txt
     * Catches invalid key and no such algorithm exceptions from hashPassword method.
     * Sets loggedIn to be true.
     * @param username users input for the password text field
     * @param password users input for the password text field
     */
    public void createNewUser(String username, String password) {
        try {
            FileWriter writeCredentialsToFile = new FileWriter(getCredentialsFile(), true);
            writeCredentialsToFile.write(username + System.lineSeparator());
            writeCredentialsToFile.write(hashPasswordWithSalt(password, createSalt()));
            writeCredentialsToFile.close();
            setLoggedIn(true);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String login(String inputtedUsername, String inputtedPassword) {
        try {
            File f = getCredentialsFile();
            Scanner readLine = new Scanner(f);
            String storedUsername = readLine.nextLine();
            String storedHash = readLine.nextLine();
            readLine.close();
            byte[] salt = getSalt(storedHash);
            String storedHashedPassword = convertStoredHashToString(storedHash);
            String hashedInputtedPassword = hashPasswordWithSalt(inputtedPassword, salt);
            System.out.println(storedHashedPassword);
            System.out.println(hashedInputtedPassword);
            if (!validateUsername(storedUsername, inputtedUsername)) {
                return "Username is incorrect";
            } else if (!validatePassword(storedHashedPassword, hashedInputtedPassword)) {
                return "Password is incorrect";
            }
        } catch (FileNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        setLoggedIn(true);;
        System.out.println("Logged in");
        return "";
    }

    public boolean validateUsername(String storedUsername, String inputtedUsername) {
        return storedUsername.equals(inputtedUsername);
    }

    public boolean validatePassword(String storedHashedPassword, String hashedInputtedPassword) {
        return storedHashedPassword.equals(hashedInputtedPassword);
    }

}
