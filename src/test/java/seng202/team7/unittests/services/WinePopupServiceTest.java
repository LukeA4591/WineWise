package seng202.team7.unittests.services;

import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.models.Wine;
import seng202.team7.services.WinePopupService;

import static org.junit.jupiter.api.Assertions.*;

public class WinePopupServiceTest {
    private static WinePopupService winePopupService;

    @BeforeEach
    void setUp() {
        winePopupService = new WinePopupService();
    }

    @Test
    void testGetImageForRedWine() {
        Wine redWine = new Wine("Red", "Plume Pinot Noir", "Lake Chalice", 2019, 80, "Marlborough", "A yummy wine");

        Image redImage = new Image(getClass().getResourceAsStream("/images/redwine.png"));
        Image image = winePopupService.getImage(redWine);
        assertNotNull(image, "Image should not be null for red wine.");
        assertEquals(redImage.getUrl(), image.getUrl());
    }

    @Test
    void testGetImageForWhiteWine() {
        Wine whiteWine = new Wine("White", "Plume Sav", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");

        Image whiteImage = new Image(getClass().getResourceAsStream("/images/whitewine.png"));
        Image image = winePopupService.getImage(whiteWine);
        assertNotNull(image, "Image should not be null for white wine.");
        assertEquals(whiteImage.getUrl(), image.getUrl());
    }

    @Test
    void testGetImageForRoseWine() {
        Wine roseWine = new Wine("Rose", "testRose", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");

        Image roseImage = new Image(getClass().getResourceAsStream("/images/whitewine.png"));
        Image image = winePopupService.getImage(roseWine);
        assertNotNull(image, "Image should not be null for white wine.");
        assertEquals(roseImage.getUrl(), image.getUrl());
    }

    @Test
    void testGetImageForUnknownWineColor() {
        Wine unknownWine = new Wine("Chardonnay", "testChardonnay", "Lake Chalice", 2019, 85, "Marlborough", "So tasty");

        Image defaultImage = new Image(getClass().getResourceAsStream("/images/whitewine.png"));
        Image image = winePopupService.getImage(unknownWine);
        assertNotNull(image, "Image should not be null for white wine.");
        assertEquals(defaultImage.getUrl(), image.getUrl());
    }
}
