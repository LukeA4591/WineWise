package seng202.team0.models;

public class Winery {
    String name;
    float latitude;
    float longitude;

    /**
     * Constructor for the Winery class
     * @param name name of the winery
     * @param latitude latitude element of the coordinate
     * @param longitude longitude element of the coordinate
     */
    public Winery(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * The Getter method for the name of the winery
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * The Getter method for the latitude of the winery
     * @return latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * The Getter method for the longitude of the winery
     * @return longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * The Setter method for the name of the winery
     * @param name name of the winery
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The Setter method for the latitude of the winery
     * @param latitude latitude of the winery
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * The Setter method for the longitude of the winery
     * @param longitude longitude of the winery
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}