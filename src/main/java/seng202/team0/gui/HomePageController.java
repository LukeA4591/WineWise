package seng202.team0.gui;

import seng202.team0.business.WineManager;
import seng202.team0.models.Wine;

import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team0.services.WinePopupService;

/**
 * Controller class for the home page of the application.
 * Handles the display of top-rated wines and interacts with the user interface elements.
 * Provides functionality to display detailed information about selected wines in a popup.
 */
public class HomePageController {

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private ImageView imageView4;
    @FXML
    private ImageView imageView5;
    @FXML
    private ImageView imageView6;
    @FXML
    private ImageView nextImage;
    @FXML
    private ImageView prevImage;
    @FXML
    private Label rating1;
    @FXML
    private Label rating2;
    @FXML
    private Label rating3;
    @FXML
    private Label rating4;
    @FXML
    private Label rating5;
    @FXML
    private Label rating6;
    @FXML
    private Label desc1;
    @FXML
    private Label desc2;
    @FXML
    private Label desc3;
    @FXML
    private Label desc4;
    @FXML
    private Label desc5;
    @FXML
    private Label desc6;
    @FXML
    private Label wine1;
    @FXML
    private Label wine2;
    @FXML
    private Label wine3;
    @FXML
    private Label wine4;
    @FXML
    private Label wine5;
    @FXML
    private Label wine6;
    @FXML
    private Label pageLabel;

    private int page;
    private Stage stage;
    private WineManager wineManager;
    private WinePopupService wineService = new WinePopupService();

    /**
     * Initializes the HomePageController. Sets the stage and loads the top 3 rated wines to be displayed with their
     * information. If there aren't 3 wines in the database, it doesn't load any.
     * @param stage The main stage passed from NavBarController.
     */
    public void init(Stage stage) {
        page = 0;
        prevImage.setVisible(false);
        wineManager = new WineManager();
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            displayWines(wines);
            displayWinery(wines);
            displayRatings(wines);
            setImage(wines);
        }
    }

    @FXML
    private void nextPage() {
        int size = wineManager.getAll().size();
        page++;
        pageLabel.setText("Page " + (page + 1));
        if (page >= 1) {
            prevImage.setVisible(true);
        }
        if ((page + 2) * 6 > size) {
            nextImage.setVisible(false);
        }
        List<Wine> wines = wineManager.getTopRated(page);
        displayWines(wines);
        displayWinery(wines);
        displayRatings(wines);
        setImage(wines);
    }

    @FXML
    private void prevPage() {
        int size = wineManager.getAll().size();
        page--;
        pageLabel.setText("Page " + (page + 1));
        if (page == 0) {
            prevImage.setVisible(false);
        }
        if ((page + 2) * 6 <= size) {
            nextImage.setVisible(true);
        }
        List<Wine> wines = wineManager.getTopRated(page);
        displayWines(wines);
        displayWinery(wines);
        displayRatings(wines);
        setImage(wines);
    }

    /**
     * Displays the names of the top-rated wines on the labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWines(List<Wine> wines) {
        wine1.setText(wines.get(0).getWineName());
        wine2.setText(wines.get(1).getWineName());
        wine3.setText(wines.get(2).getWineName());
        wine4.setText(wines.get(3).getWineName());
        wine5.setText(wines.get(4).getWineName());
        wine6.setText(wines.get(5).getWineName());
    }

    /**
     * Displays the wineries of the top-rated wines on the description labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWinery(List<Wine> wines) {
        desc1.setText("Winery: " + wines.get(0).getWineryString());
        desc2.setText("Winery: " + wines.get(1).getWineryString());
        desc3.setText("Winery: " + wines.get(2).getWineryString());
        desc4.setText("Winery: " + wines.get(3).getWineryString());
        desc5.setText("Winery: " + wines.get(4).getWineryString());
        desc6.setText("Winery: " + wines.get(5).getWineryString());
    }

    /**
     * Displays the ratings of the top-rated wines on the rating labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayRatings(List<Wine> wines) {
        rating1.setText(wines.get(0).getScore() + " / 100");
        rating2.setText(wines.get(1).getScore() + " / 100");
        rating3.setText(wines.get(2).getScore() + " / 100");
        rating4.setText(wines.get(3).getScore() + " / 100");
        rating5.setText(wines.get(4).getScore() + " / 100");
        rating6.setText(wines.get(5).getScore() + " / 100");
    }

    /**
     * Sets the images of the top-rated wines based on their color. The images are selected by using the colour of the
     * wine along with the WinePopUpService.
     * @param wines A list of the top-rated wines.
     */
    public void setImage(List<Wine> wines) {
        List<ImageView> imageViews = Arrays.asList(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6);
        for (int i = 0; i < 6; i++) {
            Image image = wineService.getImage(wines.get(i));
            ImageView imageView = imageViews.get(i);
            imageView.setImage(image);
        }
    }

    /**
     * Event handler for pressing the first wine label. Fetches the first top-rated wine and displays its details in a
     * popup.
     */
    @FXML
    void wine1Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.getFirst();
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }

    /**
     * Event handler for pressing the second wine label. Fetches the second top-rated wine and displays its details in
     * a popup.
     */
    @FXML
    void wine2Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.get(1);
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }

    /**
     * Event handler for pressing the third wine label. Fetches the third top-rated wine and displays its details in a
     * popup.
     */
    @FXML
    void wine3Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.get(2);
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }

    @FXML
    void wine4Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.get(3);
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }

    @FXML
    void wine5Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.get(4);
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }

    @FXML
    void wine6Pressed() {
        if (wineManager.getAll().size() >= 6) {
            List<Wine> wines = wineManager.getTopRated(page);
            Wine wine = wines.get(5);
            Image image = wineService.getImage(wine);
            wineService.winePressed(wine, image, rating1);
        }
    }
}