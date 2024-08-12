package seng202.team0.models;

import java.util.function.Consumer;

public class Winery {
    private final Consumer<Winery> setupScreenLauncher;

    public Winery(Consumer<Winery> setupScreenLauncher) {
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
