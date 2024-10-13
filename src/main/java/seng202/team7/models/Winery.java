package seng202.team7.models;

/**
 * Winery model, used to represent an instance of a winery.
 */
public class Winery {
    private String wineryName;
    private Float longitude;
    private Float latitude;

    /**
     * Constructor for winery with its location floats
     * @param wineryName Name of the winery
     * @param longitude Longitudinal value of the winery's location
     * @param latitude Latitudinal value of the winery's location
     */
    public Winery(String wineryName, Float longitude, Float latitude) {
        this.wineryName = wineryName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Constructor for winery without its location floats
     * @param wineryName Name of the winery
     */
    public Winery(String wineryName) {
        this.wineryName = wineryName;
        this.longitude = null;
        this.latitude = null;
    }

    /**
     * An override method for equals which allows two winery objects to be compared based on their wineryName. Since
     * each winery with have a unique name, if they both have the same name then they will be referring to the same winery.
     * @param o The winery that is being compared to the current winery
     * @return True if they are same and false if they are not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Winery winery = (Winery) o;
        return wineryName.equals(winery.wineryName);
    }

    /**
     * The Getter for the name of the winery
     * @return wineryName
     */
    public String getWineryName() {
        return wineryName;
    }

    /**
     * The Getter for the longitudinal value of the winery's location
     * @return longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * The Getter for the latitudinal value of the winery's location
     * @return latitude
     */
    public Float getLatitude() {
        return latitude;
    }

}
