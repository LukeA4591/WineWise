package seng202.team0.services;

import javafx.scene.control.Label;

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
