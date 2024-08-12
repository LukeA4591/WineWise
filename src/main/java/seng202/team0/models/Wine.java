package seng202.team0.models;

import java.util.ArrayList;
import java.util.List;

public class Wine {
    String typeWine;
    float alcoholPerc;
    int volume;
    String region;
    Winery winery;
    List<Rating> userRatings = new ArrayList<Rating>();

    /**
     * The Getter method for the type of wine
     * @return typeWine
     */
    public String getTypeWine() {
        return typeWine;
    }

    /**
     * The Getter method for the alcohol percentage of the wine
     * @return alcoholPerc
     */
    public float getAlcoholPerc() {
        return alcoholPerc;
    }

    /**
     * The Getter method for the volume of the wine
     * @return volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * The Getter method for the region of the wine
     * @return region
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
     * The Setter method for the type of wine
     * @param typeWine
     */
    public void setTypeWine(String typeWine) {
        this.typeWine = typeWine;
    }

    /**
     * The Setter method for the alcohol percentage of the wine
     * @param alcoholPerc
     */
    public void setAlcoholPerc(float alcoholPerc) {
        this.alcoholPerc = alcoholPerc;
    }

    /**
     * The Setter method for the volume of the wine
     * @param volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * The Setter method for the region the wine comes from
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * The Setter method for the winery the wine comes from
     * @param winery
     */
    public void setWinery(Winery winery) {
        this.winery = winery;
    }

    /**
     * The Setter method for the ratings which users have left on the wine
     * @param userRatings
     */
    public void setUserRatings(ArrayList<Rating> userRatings) {
        this.userRatings = userRatings;
    }
}
