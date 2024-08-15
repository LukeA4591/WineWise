package seng202.team0.gui;

import seng202.team0.services.WineEnvironment;

public class NavBarController {

    private final WineEnvironment wineEnvironment;

    /**
     * Initializer for the NavBarController, takes in a WineEnvironment
     * @param tempEnvironment WineEnvironment
     */
    public NavBarController(final WineEnvironment tempEnvironment) {
        this.wineEnvironment = tempEnvironment;
    }
}