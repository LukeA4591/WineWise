package seng202.team7.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * Service that allows passwords to be hashed and for admins to have their details stored in a file
 */
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
     * Checks if instance already exists.
     * If not creates one.
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
     * Used to track whether the admin is logged in.
     * @return getter for Boolean loggedIn
     */
    public boolean getLoginStatus() {
        return loggedIn;
    }

    /**
     * Setter for Boolean loggedIn
     * @param loggedIn A boolean value to store the login status.
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets the path of the directory containing the JAR file.
     * This path determines where to store the 'credentials.txt' file.
     * @return The parent directory of jar file as a String
     */
    public String getJarFilePath() {
        try {
        URI jarURI = AdminLoginService.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        File jarFile = new File(jarURI);
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
    public File getCredentialsFile() {
        String jarStr = this.getJarFilePath();
        jarStr += "/credentials.txt";
        File f = new File(jarStr);
        return f;
    }

    /**
     * Checks to see if the credentials.txt file has already been created
     * @return A Boolean that AppEnvironment uses to launch different screens
     */
    public boolean doesFileExist() {
        File f = getCredentialsFile();
        if (!f.exists()) {
            return false;
        }
        return true;
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
     * Checks user input for password creation
     * @param password users input for the password text field
     * @param confirmPassword users input for the confirmation password text field
     * @return error message or lack thereof
     */
    public String checkPasswordConfirmation(String password, String confirmPassword) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return "Please dont leave a field blank";
        } else if (!password.equals(confirmPassword)) {
            return "The two passwords do not match";
        } else if (password.length() < 8) {
            return "The password is under 8 characters";
        }
        return "";
    }

    /**
     * Checks user input for username creation
     * @param username The username to check.
     * @return A message indicating if the username is empty, or an empty string if the username is valid.
     */
    public String checkUsernameConfirmation(String username) {
        if (username.isEmpty()) {
            return "Please enter a username";
        }
        return "";
    }

    /**
     * Creates a random salt for password hashing.
     * Uses secure random.
     * @return The salt.
     */
    public byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }


    /**
     * Hashes password with salt and stores it.
     * Uses PBKDF2 for secure hashing.
     * The keyLength is in bits hence the 'HASH_LENGTH * 8'.
     * @param password The plaintext password to be hashed.
     * @param salt The salt used to make hash more secure.
     * @throws InvalidKeySpecException Throws exception if the key specification is not valid.
     * @throws NoSuchAlgorithmException Throws exception if the algorithm is not available.
     * @return The hashed password.
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

    /**
     * Gets the salted from stored hashed password.
     * Separates it out using arraycopy().
     * @param storedHash Base64-encoded string containing the salt and hash.
     * @return the salt.
     */
    public byte[] getSalt(String storedHash) {
        byte[] decodedBytes = Base64.getDecoder().decode(storedHash);
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(decodedBytes, 0, salt, 0, SALT_LENGTH);
        return salt;
    }

    /**
     * Creates a new user.
     * Writes the given username and hashed password to credentials.txt
     * Catches invalid key and no such algorithm exceptions from hashPassword method.
     * Sets loggedIn to be true.
     * @param username users input for the username to be stored.
     * @param password users input for the password to be hashed and stored.
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

    /**
     * Validates the user's username and password.
     * Checks entered values against stored values.
     * It does this by retrieving the salt and hashing it with user's inputted password.
     * Then compares the two values for authentication.
     * @param inputtedUsername The username inputted by the user.
     * @param inputtedPassword The plaintext password inputted by the user.
     * @return Error message or lack there-of.
     */
    public String login(String inputtedUsername, String inputtedPassword) {
        try {
            File f = getCredentialsFile();
            Scanner readLine = new Scanner(f);
            String storedUsername = readLine.nextLine();
            String storedHash = readLine.nextLine();
            readLine.close();
            byte[] salt = getSalt(storedHash);
            String hashedInputtedPassword = hashPasswordWithSalt(inputtedPassword, salt);
            if (!validateUsername(storedUsername, inputtedUsername)) {
                return "Username is incorrect";
            } else if (!validatePassword(storedHash, hashedInputtedPassword)) {
                return "Password is incorrect";
            }
        } catch (FileNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        setLoggedIn(true);
        return "";
    }

    /**
     * Validate username entered by user.
     * @param storedUsername String of the username stored.
     * @param inputtedUsername String of the username inputted by the user.
     * @return True if equal, otherwise false.
     */
    public boolean validateUsername(String storedUsername, String inputtedUsername) {
        return storedUsername.equals(inputtedUsername);
    }

    /**
     * Validate password entered by user.
     * @param storedHashedPassword String of the stored hashed password.
     * @param hashedInputtedPassword String of the hashed password inputted by the user.
     * @return True if equal, otherwise false.
     */
    public boolean validatePassword(String storedHashedPassword, String hashedInputtedPassword) {
        return storedHashedPassword.equals(hashedInputtedPassword);
    }

    /**
     * Method to change the password of the admin, creates a new file for the new credentials
     * @param currentPassword current password of the admin
     * @param newPassword new password of the admin
     * @param confirmNewPassword confirm new password of the admin
     * @return error message from password creation
     */
    public String changePassword(String currentPassword, String newPassword, String confirmNewPassword) {
        try {
            File f = getCredentialsFile();
            Scanner readLine = new Scanner(f);
            String storedUsername = readLine.nextLine();
            String storedHash = readLine.nextLine();
            readLine.close();

            byte[] salt = getSalt(storedHash);
            String hashedInputtedPassword = hashPasswordWithSalt(currentPassword, salt);

            if (!validatePassword(storedHash, hashedInputtedPassword)) {
                return "Current password is incorrect";
            }
            String validationError = checkPasswordConfirmation(newPassword, confirmNewPassword);
            if (!validationError.isEmpty()) {
                return validationError;
            }

            String hashedPassword = hashPasswordWithSalt(newPassword, createSalt());
            boolean updatedCredentials = writeUpdatedCredentials(storedUsername, hashedPassword);
            if (!updatedCredentials) {
                return "Password change failed.";
            }

        } catch (FileNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Writes the updates password to the credentials.txt file
     * @param storedUsername Username of the admin
     * @param hashedPassword New password of the admin
     * @return boolean based on the success of the password change
     */
    private boolean writeUpdatedCredentials(String storedUsername, String hashedPassword) {
        File credentialsFile = getCredentialsFile();
        File tempNewCredentialsFile = new File(credentialsFile.getParent(), "credentials_temp.txt");

        try (BufferedWriter writeCredentialsToTempFile = new BufferedWriter(new FileWriter((tempNewCredentialsFile)))) {
            writeCredentialsToTempFile.write(storedUsername);
            writeCredentialsToTempFile.newLine();
            writeCredentialsToTempFile.write(hashedPassword);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!credentialsFile.delete()) {
            tempNewCredentialsFile.delete();
            return false;
        } else {
            tempNewCredentialsFile.renameTo(credentialsFile);
            return true;
        }

    }

}
