package seng202.team0.models;

import java.util.ArrayList;
import java.util.List;

public class Wine {
    String colour;
    String wineName;
    String award;
    int score;
    int vintage;
    String region;
    Winery winery;
    String description;
    List<Rating> userRatings;

    /**
     * Constructor for the wine class
     * @param colour colour of wine
     * @param wineName name of wine
     * @param award awards won by wine
     * @param score score awarded to the wine
     * @param vintage vintage of the wine
     * @param region region the wine is from
     * @param winery winery which the wine is from
     * @param description description of the wine
     * @param userRatings ratings which the users have left on the wine
     */
    public Wine(String colour, String wineName, String award, int score, int vintage, String region, Winery winery, String description, List<Rating> userRatings) {
        this.colour = colour;
        this.wineName = wineName;
        this.award = award;
        this.score = score;
        this.vintage = vintage;
        this.region = region;
        this.winery = winery;
        this.description = description;
        this.userRatings = userRatings;
    }

    /**
     * The Getter method for the colour of wine
     * @return typeWine
     */
    public String getColor() {
        return colour;
    }

    /**
     * The Getter method for the volume of the wine
     * @return volume
     */
    public String getRegion() {
        return region;
    }

    /**
     * The Getter method for the winery which the wine comes from
     * @return winery
     */
    public Winery getWinery() {
        return winery;
    }

    /**
     * The Getter method for the ratings which users have left on the wine
     * @return userRatings
     */
    public List<Rating> getUserRatings() {
        return userRatings;
    }

    /**
     * The Getter method for the name of the wine
     * @return wineName
     */
    public String getWineName() {
        return wineName;
    }

    /**
     * The Getter method for the award that the wine has won
     * @return award
     */
    public String getAward() {
        return award;
    }

    /**
     * The Getter method for the critic score that the wine received
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * The Getter method for the vintage of the wine
     * @return vintage
     */
    public int getVintage() {
        return vintage;
    }

    /**
     * The Getter method for the description of the wine
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * The Setter method for the region the wine comes from
     * @param region region of the wine
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * The Setter method for the winery the wine comes from
     * @param winery winery which the wine is from
     */
    public void setWinery(Winery winery) {
        this.winery = winery;
    }

    /**
     * The Setter method for the ratings which users have left on the wine
     * @param userRatings ratings left by users
     */
    public void setUserRatings(ArrayList<Rating> userRatings) {
        this.userRatings = userRatings;
    }

    /**
     * The Setter method for the colour of the wine
     * @param colour colour of the wine
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * The Setter method for the name of the wine
     * @param wineName name of the wine
     */
    public void setWineName(String wineName) {
        this.wineName = wineName;
    }

    /**
     * The Setter method for the award that the wine won
     * @param award award given to the wine
     */
    public void setAward(String award) {
        this.award = award;
    }

    /**
     * The Setter method for the score that the wine received
     * @param score score given to the wine
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * The Setter method for the vintage of the wine
     * @param vintage vintage of the wine
     */
    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    /**
     * The Setter method for the description of the wine
     * @param description description of the wine
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adds a rating to the rating list of a wine
     * @param rating user rating
     */
    public void addRating(Rating rating) {
        this.userRatings.add(rating);
    }
}
