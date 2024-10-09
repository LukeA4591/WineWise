package seng202.team7.unittests.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Wine;

public class WineModelTest {

    @Test
    public void testCreateWine() {
        Wine wine = new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
        Assertions.assertEquals("Red", wine.getColor());
        Assertions.assertEquals("A red wine", wine.getWineName());
        Assertions.assertEquals("Lakes Winery", wine.getWineName());
        Assertions.assertEquals(2018, wine.getVintage());
        Assertions.assertEquals(90, wine.getScore());
        Assertions.assertEquals("Canterbury", wine.getRegion());
        Assertions.assertEquals("This is a wine from canterbury", wine.getDescription());
    }
}
