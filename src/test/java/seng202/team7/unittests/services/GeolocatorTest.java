package seng202.team7.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Position;
import seng202.team7.services.Geolocator;

public class GeolocatorTest {

    @Test
    public void testGeolocatorWithValidAddress() {
        Geolocator geolocator = new Geolocator();
        Position addressLocation = geolocator.queryAddress("20 Kirkwood Avenue");
        double longitude = addressLocation.getLng();
        double latitude = addressLocation.getLat();
        Assertions.assertEquals(172f , longitude, 1);
        Assertions.assertEquals(-43f , latitude, 1);
        Assertions.assertEquals("{\"lat\": " + latitude + ", \"lng\": " + longitude + "}", addressLocation.toString());
    }

    @Test
    public void testGeolocatorWithInvalidAddress() {
        Geolocator geolocator = new Geolocator();
        Position addressLocation = geolocator.queryAddress("I Don't Exist Street");
        double longitude = addressLocation.getLng();
        double latitude = addressLocation.getLat();
        Assertions.assertEquals(-1000f , longitude);
        Assertions.assertEquals(-1000f , latitude);
    }
}
