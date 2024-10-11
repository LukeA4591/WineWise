package seng202.team7.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.apache.poi.openxml4j.opc.PackageNamespaces;
import seng202.team7.business.ReviewManager;
import seng202.team7.business.WineManager;
import seng202.team7.models.Wine;

import java.util.*;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team7.services.WinePopupService;

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
    @FXML
    private Label noReviewsLabel;
    @FXML
    private Button sortButton;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;
    @FXML
    private Pane pane4;
    @FXML
    private Pane pane5;
    @FXML
    private Pane pane6;

    private int page;
    private int totalPages;
    private WineManager wineManager;
    private ReviewManager reviewManager;
    private List<Wine> topUserWines;
    private WinePopupService wineService = new WinePopupService();
    boolean viewCritic = true;
    boolean finalPage = false;
    private List<Pane> panes;
    private List<Label> wineLabels;
    private List<Label> descs;
    private List<Label> ratings;
    private List<ImageView> images;
    /**
     * Initializes the HomePageController. Sets the stage and loads the top 3 rated wines to be displayed with their
     * information. If there aren't 3 wines in the database, it doesn't load any.
     */
    public void init() {
        panes = new ArrayList<>(Arrays.asList(pane1, pane2, pane3, pane4, pane5, pane6));
        wineLabels = new ArrayList<>(Arrays.asList(wine1, wine2, wine3, wine4, wine5, wine6));
        descs = new ArrayList<>(Arrays.asList(desc1, desc2, desc3, desc4, desc5, desc6));
        ratings = new ArrayList<>(Arrays.asList(rating1, rating2, rating3, rating4, rating5, rating6));
        images = new ArrayList<>(Arrays.asList(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6));
        page = 0;
        prevImage.setVisible(false);
        wineManager = new WineManager();
        reviewManager = new ReviewManager();
        getTopUsers();
        validateButtons();
        checkClearScreen();
        totalPages = (wineManager.getAll().size() / 6) + 1;
        if (viewCritic) {
            if (wineManager.getAll().size() >= 6) {
                List<Wine> wines = wineManager.getTopRated(page);
                displayWines(wines);
                displayWinery(wines);
                displayRatings(wines);
                setImage(wines);
            }
        } else {
            if (topUserWines.size() >= 6) {
                List<Wine> topUserWinesPage = topUserWines.subList(page, page + 6);
                displayWines(topUserWinesPage);
                displayWinery(topUserWinesPage);
                displayRatings(topUserWinesPage);
                setImage(topUserWinesPage);
            } else {
                List<Wine> topUserWinesPage = topUserWines.subList(page, topUserWines.size());
                for (Pane pane : panes) {
                    pane.setVisible(false);
                }
                for (int i = 0; i < topUserWinesPage.size(); i++) {
                    panes.get(i).setVisible(true);
                }
                displayWines(topUserWinesPage);
                displayWinery(topUserWinesPage);
                displayRatings(topUserWinesPage);
                setImage(topUserWinesPage);
            }
        }
    }

    private void checkClearScreen() {
        if (viewCritic) {
            if (wineManager.getAll().size() == 0) {
                noReviewsLabel.setText("No reviews to display");
                for (Pane pane : panes) {
                    pane.setVisible(false);
                }
                pageLabel.setVisible(false);
                prevImage.setVisible(false);
                nextImage.setVisible(false);
            } else {
                noReviewsLabel.setText("");
                for (Pane pane : panes) {
                    pane.setVisible(true);
                }
                pageLabel.setVisible(true);
            }
        } else {
            if (topUserWines.size() == 0) {
                noReviewsLabel.setText("No reviews to display");
                for (Pane pane : panes) {
                    pane.setVisible(false);
                }
                pageLabel.setVisible(false);
                prevImage.setVisible(false);
                nextImage.setVisible(false);
            } else {
                noReviewsLabel.setText("");
                for (Pane pane : panes) {
                    pane.setVisible(true);
                }
                pageLabel.setVisible(true);
            }
        }
    }


    @FXML
    private void sortByButton() {
        if (viewCritic) {
            totalPages = (topUserWines.size() / 6) + 1;
            sortButton.setText("Customers");
            viewCritic = !viewCritic;
            page = 0;
        } else {
            totalPages = (wineManager.getAll().size() / 6) + 1;
            sortButton.setText("Critics");
            viewCritic = !viewCritic;
            page = 0;
        }
        setPage();
        init();
    }

    @FXML
    private void nextPage() {
        page++;
        validateButtons();
        setPage();
        List<Wine> wines = wineManager.getTopRated(page);
        if (viewCritic) {
            displayWines(wines);
            displayWinery(wines);
            displayRatings(wines);
            setImage(wines);
        } else {
            if (!finalPage) {
                List<Wine> topUserWinesPage = topUserWines.subList(page * 6, (page * 6) + 6);
                displayWines(topUserWinesPage);
                displayWinery(topUserWinesPage);
                displayRatings(topUserWinesPage);
                setImage(topUserWinesPage);
            } else {
                List<Wine> topUserWinesPage = topUserWines.subList(page * 6, topUserWines.size());
                for (Pane pane : panes) {
                    pane.setVisible(false);
                }
                for (int i = 0; i < topUserWinesPage.size(); i++) {
                    panes.get(i).setVisible(true);
                }
                displayWines(topUserWinesPage);
                displayWinery(topUserWinesPage);
                displayRatings(topUserWinesPage);
                setImage(topUserWinesPage);
            }
        }
    }

    @FXML
    private void prevPage() {
        if (finalPage) {
            for (Pane pane : panes) {
                pane.setVisible(true);
            }
            finalPage = !finalPage;
        }
        page--;
        validateButtons();
        setPage();
        if (viewCritic) {
            List<Wine> wines = wineManager.getTopRated(page);
            displayWines(wines);
            displayWinery(wines);
            displayRatings(wines);
            setImage(wines);
        } else {
            List<Wine> topUserWinesPage = topUserWines.subList(page * 6, (page * 6) + 6);
            displayWines(topUserWinesPage);
            displayWinery(topUserWinesPage);
            displayRatings(topUserWinesPage);
            setImage(topUserWinesPage);
        }
    }

    private void setPage() {
        pageLabel.setText("Page " + (page + 1));
    }

    private void validateButtons() {
        int size;
        if (viewCritic) {
            size = wineManager.getAll().size();
        } else {
            size = topUserWines.size();
        }
        if (page == 0) {
            prevImage.setVisible(false);
        } else {
            prevImage.setVisible(true);
        }
        if ((page + 2) * 6 <= size) {
            nextImage.setVisible(true);
        } else {
            if (!finalPage) {
                finalPageButton();
            } else {
                nextImage.setVisible(false);
            }
        }
    }

    private void finalPageButton() {
        if ((totalPages % 6) != 0 && totalPages > 6) {
            nextImage.setVisible(true);
            finalPage = true;
        } else {
            nextImage.setVisible(false);
            finalPage = false;
        }
    }

    private void getTopUsers() {
        topUserWines = new ArrayList<>();
        Wine wine;
        LinkedHashMap<Integer, Integer> avgReviews = reviewManager.getAverageReviews();
        for (Map.Entry<Integer, Integer> entry : avgReviews.entrySet()) {
            wine = wineManager.getWineFromID(entry.getKey());
            wine.setWineScore(entry.getValue());
            topUserWines.add(wine);
        }
    }

    /**
     * Displays the names of the top-rated wines on the labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWines(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label wineLabel = wineLabels.get(i);
            wineLabel.setText(wines.get(i).getWineName());
        }
    }

    /**
     * Displays the wineries of the top-rated wines on the description labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayWinery(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label desc = descs.get(i);
            desc.setText("Winery: " + wines.get(i).getWineryString());
        }
    }

    /**
     * Displays the ratings of the top-rated wines on the rating labels.
     * @param wines A list of the top-rated wines.
     */
    public void displayRatings(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Label rating = ratings.get(i);
            rating.setText(wines.get(i).getScore() + " / 100");
        }
    }

    /**
     * Sets the images of the top-rated wines based on their color. The images are selected by using the colour of the
     * wine along with the WinePopUpService.
     * @param wines A list of the top-rated wines.
     */
    public void setImage(List<Wine> wines) {
        for (int i = 0; i < wines.size(); i++) {
            Image image = wineService.getImage(wines.get(i));
            ImageView imageView = images.get(i);
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