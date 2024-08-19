package seng202.team0.services;

import java.util.function.Consumer;

public class WineEnvironment {
    private final Consumer<WineEnvironment> setupScreenLauncher;
    private final Consumer<WineEnvironment> navBarLauncher;

    public WineEnvironment(Consumer<WineEnvironment> setupScreenLauncher, Consumer<WineEnvironment> navBarLauncher) {
        this.setupScreenLauncher = setupScreenLauncher;
        this.navBarLauncher = navBarLauncher;
        launchNavBar();
    }

    /**
     * Launches the setup screen.
     */
    public void launchSetupScreen() {
        setupScreenLauncher.accept(this);
    }

    /**
     * Launches the nav bar
     */
    public void launchNavBar() {
        navBarLauncher.accept(this);
    }
}
