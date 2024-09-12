package seng202.team0.services;

import java.util.function.Consumer;

public class WineEnvironment {

    /**
     * Consumer that is used to launch nav bar
     */
    private final Consumer<WineEnvironment> navBarLauncher;

    /**
     * Consumer that is used to launch admin setup screen.
     */
    private final Consumer<WineEnvironment> adminSetupScreenLauncher;

    /**
     * Consumer that is used to launch admin screen.
     */
    private final Consumer<WineEnvironment> adminScreenLauncher;

    /**
     * Consumer that is used to launch add wine screen.
     */
    private final Consumer<WineEnvironment> addWineScreenLauncher;

    /**
     * Consumer that is used to launch add winery screen.
     */
    private final Consumer<WineEnvironment> addWineryScreenLauncher;

    /**
     * Runnable that clears the current page.
     */
    private final Runnable clear;
    /**
     * The singleton instance of AdminLoginService.
     */
    private final AdminLoginService adminLoginInstance;

    /**
     * WineEnvironment keeps track of the state of the program
     * @param navBarLauncher Launches the nav bar
     * @param adminSetupScreenLauncher Launches the admin setup screen
     * @param adminScreenLauncher Launches the admin screen
     * @param clear Runnable to clear the page
     */
    public WineEnvironment(Consumer<WineEnvironment> navBarLauncher, Consumer<WineEnvironment> adminSetupScreenLauncher,
                           Consumer<WineEnvironment> adminScreenLauncher, Consumer<WineEnvironment> addWineScreenLauncher,
                           Consumer<WineEnvironment> addWineryScreenLauncher, Runnable clear) {
        this.adminLoginInstance = AdminLoginService.getInstance();
        this.navBarLauncher = navBarLauncher;
        this.adminSetupScreenLauncher = adminSetupScreenLauncher;
        this.adminScreenLauncher = adminScreenLauncher;
        this.addWineScreenLauncher = addWineScreenLauncher;
        this.addWineryScreenLauncher = addWineryScreenLauncher;
        this.clear = clear;
        boolean isFirstRun = adminLoginInstance.createCredentialsFileIfNotExists();
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
     * Launches the add wine screen
     */
    public void launchAddWineScreen() {
        addWineScreenLauncher.accept(this);
    }

    /**
     * Launches the add winery screen
     */
    public void launchAddWineryScreen() {
        addWineryScreenLauncher.accept(this);
    }

    /**
     *
     * @return returns the clear runnable that controllers use to clear the page
     */
    public Runnable getClearRunnable() {
        return clear;
    }

}


