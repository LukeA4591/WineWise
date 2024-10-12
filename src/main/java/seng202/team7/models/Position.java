package seng202.team7.models;

/**
 * Position model class, used to place wineries
 */
public class Position {
    private double lat;
    private double lng;

    /**
     * Constructor for the position class
     * @param lat latitude of the position
     * @param lng longitude of the position
     */
    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    /**
     * Getter for the longitude of the position
     * @return double longitude
     */
    public double getLng() {
        return lng;
    }

    /**
     * Overrided toString method
     * @return String of the position
     */
    public String toString() {
        return "{\"lat\": " + lat + ", \"lng\": " + lng + "}";
    }
}
