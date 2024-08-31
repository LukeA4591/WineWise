package seng202.team0.unittests.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team0.services.AdminLoginService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Testing Admin Login
 * @author Felix Blanchard
 */
public class AdminLoginServiceTest {

    private static AdminLoginService testAdminLoginService;
    private Path testDirectory;

    /**
     * Setup before all the tests, we create the single instance of AdminLoginService
     */
    @BeforeAll
    public static void setupTest() {
        testAdminLoginService = AdminLoginService.getInstance();
    }

    @BeforeEach
    public void setupTestDirectory() {
        testDirectory = Paths.get(System.getProperty("user.dir"), "testDirectory");
        try {
            Files.createDirectories(testDirectory);
        } catch (IOException e) {
            fail("Failed to create a test directory");
        }
        System.setProperty("user.dir", testDirectory.toString());
    }

    @AfterEach
    public void deleteTestDirectory() {
        Path credentialsFilePath = testDirectory.resolve("credentials.txt");
        try {
            Files.deleteIfExists(credentialsFilePath);
        } catch (IOException e) {
            fail("Failed to delete test directory");
        }
    }

    /**
     * Tests that AdminLoginService is a singleton
     * Checks that both references point to the same instance
     */
    @Test
    public void testSingletonInstance() {
        AdminLoginService extraAdminLoginService = AdminLoginService.getInstance();
        assertSame(testAdminLoginService, extraAdminLoginService);
    }

    /**
     * Tests that the initial state of loggedIn is false
     */
    @Test
    public void testInitialLoginStatus() {
        assertFalse(testAdminLoginService.getLoginStatus());
    }

    /**
     * Tests the creation / existence of credentials.txt
     * Before the test any previously created credentials.txt are deleted.
     */
    @Test
    public void testCreatingCredentialsFile() {
        assertFalse(testAdminLoginService.createCredentialsFileIfNotExists());
        assertTrue(testAdminLoginService.createCredentialsFileIfNotExists());
    }


//    @Test
//    public void testGettingJarFilePath() {
//
//    }

}
