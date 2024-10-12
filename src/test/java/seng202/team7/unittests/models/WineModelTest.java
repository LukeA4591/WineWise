package seng202.team7.unittests.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Wine;

public class WineModelTest {

    private Wine createWine() {
        return new Wine("Red", "A red wine", "Lakes Winery", 2018, 90, "Canterbury", "This is a wine from canterbury");
    }

    @Test
    public void testCreateWine() {
        Wine wine = createWine();
        Assertions.assertEquals("Red", wine.getColor());
        Assertions.assertEquals("A red wine", wine.getWineName());
        Assertions.assertEquals("Lakes Winery", wine.getWineryString());
        Assertions.assertEquals(2018, wine.getVintage());
        Assertions.assertEquals(90, wine.getScore());
        Assertions.assertEquals("Canterbury", wine.getRegion());
        Assertions.assertEquals("This is a wine from canterbury", wine.getDescription());
        wine.setWineScore(40);
        Assertions.assertEquals(40, wine.getScore());
    }

    @Test
    public void testWineObjectEquals() {
        Wine firstWine = createWine();
        Wine secondWine = createWine();
        Assertions.assertEquals(firstWine, secondWine);
    }
}
