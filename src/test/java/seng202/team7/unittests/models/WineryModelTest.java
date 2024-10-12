package seng202.team7.unittests.models;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Winery;

public class WineryModelTest {

    private Winery createWineryWithLocation() {
        return new Winery("First Winery", 1f, 2f);
    }

    private Winery createWineryNoLocation() {
        return new Winery("First Winery");
    }

    @Test
    public void testCreateWineryWithLocation() {
        Winery wineryWithLocation = createWineryWithLocation();
        Assertions.assertEquals("First Winery", wineryWithLocation.getWineryName());
        Assertions.assertEquals(1f, wineryWithLocation.getLongitude());
        Assertions.assertEquals(2f, wineryWithLocation.getLatitude());
    }

    @Test
    public void testCreateWineryWithoutLocation() {
        Winery wineryWithoutLocation = createWineryNoLocation();
        Assertions.assertEquals("First Winery", wineryWithoutLocation.getWineryName());
        Assertions.assertNull(wineryWithoutLocation.getLongitude());
        Assertions.assertNull(wineryWithoutLocation.getLatitude());
    }

    @Test
    public void testWineryObjectsEquals() {
        Winery firstWineryWithLocation = createWineryWithLocation();
        Winery secondWineryWithLocation = createWineryWithLocation();
        Winery wineryWithoutLocation = createWineryNoLocation();
        Assertions.assertEquals(firstWineryWithLocation, secondWineryWithLocation);
        Assertions.assertEquals(firstWineryWithLocation, wineryWithoutLocation);
    }
}
