package seng202.team7.services;

import java.time.Year;

/**
 * Service class for the Wine model
 */
public class WineService {

    /**
     * Default constructor for the WineService
     */
    public WineService() {

    }

    /**
     * Validates a wine depending on whether the user inputted string are valid for the database
     * @param wineName name of the wine
     * @param wineryName name of the winery
     * @param vintage vintage of the wine
     * @param score score of the wine
     * @param wineRegion region of the wine
     * @param wineDescription description of the wine
     * @return an empty string if it is a valid wine, otherwise a string for an error label
     */
    public String validateWine(String wineName, String wineryName, String vintage, String score, String wineRegion, String wineDescription) {
        if (wineryName.isEmpty()) {
            return "Winery Name field is empty.";
        } else if (wineName.isEmpty()){
            return "Wine Name field is empty.";
        } else if (vintage.isEmpty()) {
            return "Wine Vintage field is empty.";
        } else {
            String intErrorLabel = checkIntegers(vintage, score);

            if (!intErrorLabel.equals("")) {
                return intErrorLabel;
            }

            if (wineryName.length() > 100 || wineName.length() > 100 || wineRegion.length() > 100 || wineDescription.length() > 1000) {
                return "Text fields are too long.";
            }

            return "";
        }

    }

    /**
     * Helper function to check the integer values are valid
     * @param vintage vintage of wine
     * @param score score of wine
     * @return an empty string if it is a valid wine, otherwise a string for an error label
     */
    private String checkIntegers(String vintage, String score) {
        try {
            int wineVintageInt = Integer.parseInt(vintage);
            if (wineVintageInt < 0 || wineVintageInt > Year.now().getValue()) {
                return "Vintage should be between 0 and the current year.";
            }

            else if (!score.isEmpty()) {
                int wineScoreInt = Integer.parseInt(score);
                if (wineScoreInt < 0 || wineScoreInt > 100) {
                    return "Score should be between 0-100.";
                }
            }

            return "";
        } catch (NumberFormatException e) {
            return "Wine Vintage and Score should be a number.";
        }
    }
}
