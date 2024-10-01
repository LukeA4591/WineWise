package seng202.team0.models;

public class Winery {
    private String wineryName;
    private Float longitude;
    private Float latitude;

    public Winery(String wineryName, Float longitude, Float latitude) {
        this.wineryName = wineryName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Winery(String wineryName) {
        this.wineryName = wineryName;
        this.longitude = null;
        this.latitude = null;
    }

    public String getWineryName() {
        return wineryName;
    }

    public void setWineryName(String wineryName) {
        this.wineryName = wineryName;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}
