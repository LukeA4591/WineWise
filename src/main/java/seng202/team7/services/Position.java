package seng202.team7.services;

public class Position {
    double lat;
    double lng;
    public Position(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public String toString() {
        return "{\"lat\": " + lat + ", \"lng\": " + lng + "}";
    }
}
