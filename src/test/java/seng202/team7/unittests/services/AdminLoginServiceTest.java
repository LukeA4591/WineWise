package seng202.team7.unittests.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import seng202.team7.services.AdminLoginService;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Testing Admin Login implementation
 * @author Felix Blanchard
 */
public class AdminLoginServiceTest {

    private static AdminLoginService testAdminLoginService;
    private static final int SALT_LENGTH = 16;

    /**
     * Setup before all the tests, we create the single instance of AdminLoginService
     */
    @BeforeAll
    public static void setupTest() {
        testAdminLoginService = AdminLoginService.getInstance();
    }

    /**
     * Deletes credentials file between tests.
     * Resets the login status to false.
     */
    @AfterEach
    public void deleteCredentialsFile() {
        File credentialsFile = testAdminLoginService.getCredentialsFile();
        if (credentialsFile.exists()) {
            credentialsFile.delete();
        }
        testAdminLoginService.setLoggedIn(false);
    }

    /**
     * Tests that AdminLoginService is a singleton
     * Checks that both references point to the same instance
     * Tests that the initial state of loggedIn is false
     */
    @Test
    public void testSingletonInstance() {
        AdminLoginService extraAdminLoginService = AdminLoginService.getInstance();
        assertSame(testAdminLoginService, extraAdminLoginService);
        assertFalse(testAdminLoginService.getLoginStatus());
    }


    /**
     * Tests the creation / existence of credentials.txt
     * Before the test any previously created credentials.txt are deleted.
     */
    @Test
    public void testCreatingCredentialsFile() {
        assertFalse(testAdminLoginService.doesFileExist());
        testAdminLoginService.createCredentialsFile();
        assertTrue(testAdminLoginService.doesFileExist());
    }

    /**
     * Tests that if username fields are left blank, the expected error message is returned.
     */
    @Test
    public void testBlankUsernameConfirmation() {
        assertEquals("Please enter a username", testAdminLoginService.checkUsernameConfirmation(""));
    }

    /**
     * Tests that if various fields are left blank, the expected error message is returned.
     */
    @Test
    public void testBlankPasswordConfirmation() {
        assertEquals("Please dont leave a field blank", testAdminLoginService.checkPasswordConfirmation("", ""));
        assertEquals("Please dont leave a field blank", testAdminLoginService.checkPasswordConfirmation("password", ""));
        assertEquals("Please dont leave a field blank", testAdminLoginService.checkPasswordConfirmation("", "password"));
    }

    /**
     * Tests that if the passwords don't match, the expected error message is returned.
     */
    @Test
    public void testNonMatchingPasswordConfirmation() {
        assertEquals("The two passwords do not match", testAdminLoginService.checkPasswordConfirmation("password", "pass"));
    }

    /**
     * Tests that if the password is too short, the expected error message is returned.
     */
    @Test
    public void testTooShortPasswordConfirmation() {
        assertEquals("The password is under 8 characters", testAdminLoginService.checkPasswordConfirmation("pass", "pass"));
    }

    /**
     * Tests that for a valid password, the app doesn't return a error message.
     */
    @Test
    public void testSuccessfulPasswordConfirmation() {
        assertEquals("", testAdminLoginService.checkPasswordConfirmation("password", "password"));
    }

    /**
     * Tests that the createSalt() method creates a salt of the expected length.
     */
    @Test
    public void testSaltIsCorrectLength() {
        assertEquals(SALT_LENGTH, testAdminLoginService.createSalt().length);
    }

    /**
     * Tests that the hashPasswordWithSalt is always returning a result.
     * This implies that the hashing function is working.
     * Note that it doesn't test if output is excepted.
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws NoSuchAlgorithmException if the algorithm for hashing is not available
     */
    @Test
    public void testNotNullHashPasswordWithSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "plaintext";
        byte[] salt = testAdminLoginService.createSalt();
        assertNotNull(testAdminLoginService.hashPasswordWithSalt(password, salt));
    }

    /**
     * This test checks that the hashing function produces a consistent hash output,
     * given a predetermined salt and password.
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws NoSuchAlgorithmException if the algorithm for hashing is not available
     */
    @Test
    public void testExpectedHashPasswordWithSalt() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "plaintext";
        byte[] salt = new byte[SALT_LENGTH];
        for (int i = 0; i < salt.length; i ++) { salt[i] = (byte) i; }
        String expectedHash = "AAECAwQFBgcICQoLDA0OD+i6gT7lEihdhfiuXmVilEupS9P7/wG2bgxdd8f759hE";
        String actualHash = testAdminLoginService.hashPasswordWithSalt(password, salt);
        assertEquals(expectedHash, actualHash);
    }

    /**
     * Tests that for valid inputs, createNewUser doesn't throw a IOException, InvalidKeySpecException or NoSuchAlgorithmException
     */
    @Test
    public void testNoExceptionsThrownCreateNewUser() {
        assertDoesNotThrow(() -> testAdminLoginService.createNewUser("username", "password"));
    }

    /**
     * Tests that given invalid usernames the login method returns the correct error message.
     * And that the state of loggedIn is still false,
     * ensuring that admin restricted actions cannot be undertaken.
     */
    @Test
    public void testInvalidUsernameLogin() {
        testAdminLoginService.createNewUser("username", "password");
        assertTrue(testAdminLoginService.getLoginStatus());
        testAdminLoginService.setLoggedIn(false);
        assertEquals("Username is incorrect", testAdminLoginService.login("invalid", "password"));
        assertFalse(testAdminLoginService.getLoginStatus());
    }

    /**
     * Tests that given an invalid password the login method returns the correct error message.
     * And that the state of loggedIn is still false,
     * ensuring that admin restricted actions cannot be undertaken.
     */
    @Test
    public void testInvalidPasswordLogin() {
        testAdminLoginService.createNewUser("username", "password");
        assertTrue(testAdminLoginService.getLoginStatus());
        testAdminLoginService.setLoggedIn(false);
        assertEquals("Password is incorrect", testAdminLoginService.login("username", "invalid"));
        assertFalse(testAdminLoginService.getLoginStatus());
    }

    /**
     * Tests that given valid credentials the login method doesn't return an error message.
     * And that the state of loggedIn is set to true,
     * so that admin restricted actions can be performed.
     */
    @Test
    public void testValidCredentialsLogin() {
        testAdminLoginService.createNewUser("username", "password");
        assertTrue(testAdminLoginService.getLoginStatus());
        testAdminLoginService.setLoggedIn(false);
        assertEquals("", testAdminLoginService.login("username", "password"));
        assertTrue(testAdminLoginService.getLoginStatus());
    }

    @Test
    public void testUpdateCredentialsMatchingPasswords() {
        testAdminLoginService.createNewUser("username", "password");
        String errorString = testAdminLoginService.changePassword("password", "newpassword", "newpassword");
        assertEquals("", errorString);

        testAdminLoginService.setLoggedIn(false);
        testAdminLoginService.login("username", "newpassword");
        assertTrue(testAdminLoginService.getLoginStatus());

        testAdminLoginService.setLoggedIn(false);
        testAdminLoginService.login("username", "password");
        assertFalse(testAdminLoginService.getLoginStatus());
    }

    @Test
    public void testUpdateCredentialsDifferentPasswords() {
        testAdminLoginService.createNewUser("username", "password");
        String errorString = testAdminLoginService.changePassword("password", "newpassword1", "newpassword2");
        assertEquals("The two passwords do not match", errorString);

        testAdminLoginService.setLoggedIn(false);
        testAdminLoginService.login("username", "newpassword");
        assertFalse(testAdminLoginService.getLoginStatus());

        testAdminLoginService.login("username", "password");
        assertTrue(testAdminLoginService.getLoginStatus());
    }

    @Test
    public void testUpdateCredentialsWrongCurrentPassword() {
        testAdminLoginService.createNewUser("username", "password");
        String errorString = testAdminLoginService.changePassword("wrongpassword", "newpassword", "newpassword");
        assertEquals("Current password is incorrect", errorString);

        testAdminLoginService.setLoggedIn(false);
        testAdminLoginService.login("username", "newpassword");
        assertFalse(testAdminLoginService.getLoginStatus());

        testAdminLoginService.login("username", "password");
        assertTrue(testAdminLoginService.getLoginStatus());
    }

}
