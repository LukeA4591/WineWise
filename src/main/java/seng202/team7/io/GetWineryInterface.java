package seng202.team7.io;

/**
 * Interface to get wineries for the map
 */
public interface GetWineryInterface {

    /**
     * A promised method for the GetWineryInterface
     * @param wineryName name of the winery
     * @return boolean for success
     */
    boolean operation(String wineryName);
}
