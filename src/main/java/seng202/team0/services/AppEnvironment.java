package seng202.team0.services;

import java.util.function.Consumer;

public class AppEnvironment {

    /**
     * Consumer that is used to launch nav bar
     */
    private final Consumer<AppEnvironment> navBarLauncher;

    /**
     * Consumer that is used to launch admin setup screen.
     */
    private final Consumer<AppEnvironment> adminSetupScreenLauncher;

    /**
     * Consumer that is used to launch admin screen.
     */
    private final Consumer<AppEnvironment> adminScreenLauncher;

    /**
     * Runnable that clears the current page.
     */
    private final Runnable clear;
    /**
     * The singleton instance of AdminLoginService.
     */
    private final AdminLoginService adminLoginInstance;

    /**
     * AppEnvironment keeps track of the state of the program
     * @param navBarLauncher Launches the nav bar
     * @param adminSetupScreenLauncher Launches the admin setup screen
     * @param adminScreenLauncher Launches the admin screen
     * @param clear Runnable to clear the page
     */
    public AppEnvironment(Consumer<AppEnvironment> navBarLauncher, Consumer<AppEnvironment> adminSetupScreenLauncher,
                          Consumer<AppEnvironment> adminScreenLauncher, Runnable clear) {
        this.adminLoginInstance = AdminLoginService.getInstance();
        this.navBarLauncher = navBarLauncher;
        this.adminSetupScreenLauncher = adminSetupScreenLauncher;
        this.adminScreenLauncher = adminScreenLauncher;
        this.clear = clear;
        boolean isFirstRun = adminLoginInstance.doesFileExist();
        if(!isFirstRun) {
            // launch create new account screen
            launchAdminSetupScreen();
        } else {
            // launch main screen code
            launchNavBar();
        }


    }

    /**
     * Getter for the singleton instance of AdminLoginService
     * @return singleton instance of AdminLoginService
     */
    public AdminLoginService getAdminLoginInstance() {
        return adminLoginInstance;
    }

    /**
     * Launches the nav bar
     */
    public void launchNavBar() {
        navBarLauncher.accept(this);
    }

    /**
     * Launches the admin setup screen
     */
    public void launchAdminSetupScreen() {
        adminSetupScreenLauncher.accept(this);
    }

    /**
     * Launches the admin screen
     */
    public void launchAdminScreen() {
        adminScreenLauncher.accept(this);
    }

    /**
     * Gets the clear runnable that controllers use to clear the page.
     * @return returns the clear runnable.
     */
    public Runnable getClearRunnable() {
        return clear;
    }

}


