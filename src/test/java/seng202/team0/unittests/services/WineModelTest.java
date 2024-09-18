package seng202.team0.unittests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team0.models.Wine;

public class WineModelTest {

    @Test
    public void testCreateWine() {
        Wine wine = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
        Assertions.assertEquals(wine.getColor(), "Red");
        Assertions.assertEquals(wine.getWineName(), "A red wine");
        Assertions.assertEquals(wine.getWineryString(), "Lakes Winery");
        Assertions.assertEquals(wine.getVintage(), 2018);
        Assertions.assertEquals(wine.getScore(), 90);
        Assertions.assertEquals(wine.getRegion(), "Canterbury");
        Assertions.assertEquals(wine.getDescription(), "This is a wine from canterbury");
    }
}
