package seng202.team7.models;

public class Position {
    private double lat;
    private double lng;
    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    private double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public String toString() {
        return "{\"lat\": " + lat + ", \"lng\": " + lng + "}";
    }
}
