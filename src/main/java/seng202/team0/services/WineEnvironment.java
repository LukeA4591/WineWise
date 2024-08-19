package seng202.team0.services;

import java.util.function.Consumer;

public class WineEnvironment {
    private final Consumer<WineEnvironment> setupScreenLauncher;
    private final Consumer<WineEnvironment> adminSetupScreenLauncher;
    private final Consumer<WineEnvironment> adminScreenLauncher;
    private final Runnable clear;
    private final AdminLoginService adminLoginInstance;

    public WineEnvironment(Consumer<WineEnvironment> setupScreenLauncher, Consumer<WineEnvironment> adminSetupScreenLauncher, Consumer<WineEnvironment> adminScreenLauncher, Runnable clear) {
        this.adminLoginInstance = AdminLoginService.getInstance();
        this.setupScreenLauncher = setupScreenLauncher;
        this.adminSetupScreenLauncher = adminSetupScreenLauncher;
        this.adminScreenLauncher = adminScreenLauncher;
        this.clear = clear;
        boolean isFirstRun = adminLoginInstance.createCredentialsFileIfNotExists();
        if(!isFirstRun) {
            // launch create new account screen
            launchAdminSetupScreen();
        } else {
            // launch main screen code
            launchSetupScreen();
        }
    }

    public AdminLoginService getAdminLoginInstance() {
        return adminLoginInstance;
    }

    /**
     * Launches the setup screen.
     */
    public void launchSetupScreen() {
        setupScreenLauncher.accept(this);
    }

    public void launchAdminSetupScreen() {
        adminSetupScreenLauncher.accept(this);
    }

    public void launchAdminScreen() {
        adminScreenLauncher.accept(this);
    }

    public Runnable getClearRunnable() {
        return clear;
    }

}


