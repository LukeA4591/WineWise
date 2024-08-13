package seng202.team0.services;

import java.util.function.Consumer;

public class WineEnvironment {
    private final Consumer<WineEnvironment> setupScreenLauncher;

    public WineEnvironment(Consumer<WineEnvironment> setupScreenLauncher) {
        this.setupScreenLauncher = setupScreenLauncher;
        launchSetupScreen();
    }

    /**
     * Launches the setup screen.
     */
    public void launchSetupScreen() {
        setupScreenLauncher.accept(this);
    }
}
