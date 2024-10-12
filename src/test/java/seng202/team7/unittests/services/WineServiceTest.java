package seng202.team7.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team7.services.WineService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineServiceTest {

    private WineService wineService;
    private String winery;
    private String name;
    private String vintage;
    private String score;
    private String region;
    private String description;
    private String errorMessage;

    @BeforeEach
    public void setup() {
        wineService = new WineService();
    }

    @Test
    public void validateWineSuccess() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "2022";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("", errorMessage);
    }

    @Test
    public void validateWineEmptyWinery() {
        winery = "";
        name = "This is a wine";
        vintage = "2022";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Winery Name field is empty.", errorMessage);
    }

    @Test
    public void validateWineEmptyName() {
        winery = "This is a winery";
        name = "";
        vintage = "2022";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Wine Name field is empty.", errorMessage);
    }

    @Test
    public void validateWineEmptyVintage() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Wine Vintage field is empty.", errorMessage);
    }

    @Test
    public void validateWineLongWinery() {
        winery = "Blah blah blah blah Blah blah blah blah Blah blah blah blah Blah blah blah blah Blah blah blah blah Blah blah blah blah Blah blah blah blah Blah blah blah blah";
        name = "This is a wine";
        vintage = "2022";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Text fields are too long.", errorMessage);
    }

    @Test
    public void validateWineFutureVintage() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "9999";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Vintage should be between 0 and the current year.", errorMessage);
    }

    @Test
    public void validateWineBigScore() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "2022";
        score = "110";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Score should be between 0-100.", errorMessage);
    }

    @Test
    public void validateWineSmallScore() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "2022";
        score = "-20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Score should be between 0-100.", errorMessage);
    }

    @Test
    public void validateWineCharsInVintage() {
        winery = "This is a winery";
        name = "This is a wine";
        vintage = "2022 omg that's a char";
        score = "20";
        region = "Canterbury";
        description = "This is a wine from canterbury";
        errorMessage = wineService.validateWine(name ,winery, vintage, score, region, description);
        assertEquals("Wine Vintage and Score should be a number.", errorMessage);
    }
}
